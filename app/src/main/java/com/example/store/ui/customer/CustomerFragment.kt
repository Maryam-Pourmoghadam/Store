package com.example.store.ui.customer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.data.network.NetworkResult
import com.example.store.databinding.FragmentCustomerBinding
import com.example.store.model.Billing
import com.example.store.model.CustomerItem
import com.example.store.model.SharedFunctions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerFragment : Fragment() {
    private lateinit var binding: FragmentCustomerBinding
    private val customerViewModel: CustomerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            customerViewModel.address = it.getString("address", "")
            customerViewModel.navigatedFromCInfoFrgmnt = it.getBoolean("navigatedFromCustomerInfo", false)
        }
    }

    override fun onResume() {
        super.onResume()
        if (customerViewModel.address.isNotBlank()) {
            binding.tvAddress.text = customerViewModel.address
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLiveDatas(view)
        setButtonsListener()

    }

    private fun initViews(){
        val sharedPref = activity?.getSharedPreferences("customer input data", Context.MODE_PRIVATE)
        if (sharedPref!=null)
        {
            binding.etName.setText(sharedPref.getString("name",""))
            binding.etFamily.setText(sharedPref.getString("family",""))
            binding.etEmail.setText(sharedPref.getString("email",""))
        }
    }

    private fun observeLiveDatas(view: View){
        customerViewModel.customerWithId.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.loadingView.visibility=View.GONE
                    enableButtons()
                    response.data.let { customer ->
                        customerViewModel.setCustomerInSharedPref(requireActivity(), customer)
                        SharedFunctions.showSnackBar("مشتری ذخیره شد",view)
                        customerViewModel.clearInputDataSharedPref(requireContext())
                        if (customerViewModel.navigatedFromCInfoFrgmnt) {
                            findNavController().navigate(R.id.action_customerFragment_to_customerInfoFragment)
                        } else {
                            findNavController().navigate(R.id.action_customerFragment_to_shoppingCartFragment)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    binding.loadingView.visibility=View.GONE
                    enableButtons()
                    SharedFunctions.showSnackBar( "${response.message.toString()} متاسفانه مشتری ثبت نشد , ",view)
                }
                is NetworkResult.Loading -> {
                    binding.loadingView.visibility=View.VISIBLE
                    disableButtons()
                    SharedFunctions.showSnackBar("جهت ثبت مشتری منتظر بمانید",view)
                }
            }
        }

    }

    private fun setButtonsListener(){
        binding.btnRegisterCustomer.setOnClickListener {
            if (areValidInputs()) {
                customerViewModel.customer =
                    CustomerItem(
                        0, binding.etEmail.text.toString(),
                        binding.etName.text.toString(),
                        binding.etFamily.text.toString(), Billing(customerViewModel.address)
                    )
                customerViewModel.registerCustomer(customerViewModel.customer!!)
            }
        }

        binding.btnAddAddressByMap.setOnClickListener {
            customerViewModel.setInputDataInShared(
                requireActivity(), binding.etName.text.toString(),
                binding.etFamily.text.toString(), binding.etEmail.text.toString()
            )
            val action = CustomerFragmentDirections
                .actionCustomerFragmentToAddAddressFragment("", customerViewModel.navigatedFromCInfoFrgmnt)
            findNavController().navigate(action)
        }
    }

    private fun enableButtons(){
        binding.btnAddAddressByMap.isEnabled=true
        binding.btnRegisterCustomer.isEnabled=true
    }

    private fun disableButtons(){
        binding.btnAddAddressByMap.isEnabled=false
        binding.btnRegisterCustomer.isEnabled=false
    }

    private fun areValidInputs(): Boolean {
        if (binding.etName.text.isNullOrBlank()) {
            binding.etName.error = "فیلد را پر کنید"
            return false
        }

        if (binding.etFamily.text.isNullOrBlank()) {
            binding.etFamily.error = "فیلد را پر کنید"
            return false
        }
        if (binding.etEmail.text.isNullOrBlank()) {
            binding.etEmail.error = "فیلد را پر کنید"
            return false
        }
        if (!binding.etEmail.text.matches(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"))) {
            binding.etEmail.error = "فرم ایمیل صحیح نمی باشد"
            return false
        }
        if (customerViewModel.address.isBlank()) {
            Toast.makeText(requireContext(), "لطفا یک آدرس انتخاب کنید", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}