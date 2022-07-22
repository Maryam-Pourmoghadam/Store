package com.example.store.ui.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentCustomerBinding
import com.example.store.model.Billing
import com.example.store.model.CustomerItem
import com.example.store.model.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerFragment : Fragment() {
    lateinit var binding: FragmentCustomerBinding
    private val customerViewModel: CustomerViewModel by viewModels()
    var customer: CustomerItem? = null
    var address: String = ""
    var navigatedFromCInfoFrgmnt = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            address = it.getString("address", "")
            navigatedFromCInfoFrgmnt = it.getBoolean("navigatedFromCustomerInfo", false)
        }
    }

    override fun onResume() {
        super.onResume()
        if (address.isNotBlank()) {
            binding.tvAddress.text = address
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
        customerViewModel.customerWithId.observe(viewLifecycleOwner) {
            if (it != null) {
                customerViewModel.setCustomerInSharedPref(requireActivity(), it)
                Snackbar.make(
                    view, "مشتری ذخیره شد",
                    Snackbar.LENGTH_LONG
                ).setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                    .show()
                if (navigatedFromCInfoFrgmnt) {
                    findNavController().navigate(R.id.action_customerFragment_to_customerInfoFragment)
                } else {
                    findNavController().navigate(R.id.action_customerFragment_to_shoppingCartFragment)
                }
            }
        }
        customerViewModel.status.observe(viewLifecycleOwner) {
            setUIbyStatus(it)
        }

        binding.btnRegisterCustomer.setOnClickListener {
            if (areValidInputs()) {
                customer =
                    CustomerItem(
                        0, binding.etEmail.text.toString(),
                        binding.etName.text.toString(),
                        binding.etFamily.text.toString(), Billing(address)
                    )
                Toast.makeText(requireContext(), "جهت ثبت مشتری منتظر بمانید", Toast.LENGTH_SHORT)
                    .show()
                customerViewModel.registerCustomer(customer!!)
            }
        }

        binding.btnRetryCustomer.setOnClickListener {
            customer?.let { it1 -> customerViewModel.registerCustomer(it1) }
        }
        binding.btnAddAddressByMap.setOnClickListener {
            val action=CustomerFragmentDirections
                .actionCustomerFragmentToAddAddressFragment("",navigatedFromCInfoFrgmnt)
            findNavController().navigate(action)
        }
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
        if (address.isBlank()) {
            Toast.makeText(requireContext(), "لطفا یک آدرس انتخاب کنید", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun setUIbyStatus(status: Status) {
        when (status) {
            Status.ERROR -> {
                binding.llErrorConnectionCustomer.visibility = View.VISIBLE
                binding.llCreateCustomer.visibility = View.GONE
            }
            Status.LOADING -> {
                binding.llErrorConnectionCustomer.visibility = View.GONE
                binding.llCreateCustomer.visibility = View.VISIBLE
                // binding.shimmerLayout.visibility = View.VISIBLE

            }
            else -> {
                binding.llErrorConnectionCustomer.visibility = View.GONE
                binding.llCreateCustomer.visibility = View.VISIBLE
                // binding.shimmerLayout.visibility = View.GONE
            }
        }
    }
}