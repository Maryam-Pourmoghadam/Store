package com.example.store.ui.customerInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.data.network.NetworkResult
import com.example.store.databinding.FragmentCustomerInfoBinding
import com.example.store.model.Billing
import com.example.store.model.CustomerItem
import com.example.store.model.SharedFunctions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerInfoFragment : Fragment() {
    lateinit var binding: FragmentCustomerInfoBinding
    private val customerInfoViewModel: CustomerInfoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setValuesAndInitViews()
        observeLiveDatas(view)
        setListeners()
    }

    private fun setValuesAndInitViews() {
        val savedCustomer = customerInfoViewModel.getCustomerFromSharedPref(requireActivity())
        if (savedCustomer != null) {
            binding.llCustomerInfo.visibility = View.VISIBLE
            binding.llNoCustomer.visibility = View.GONE
            binding.tvNameCinfo.text = savedCustomer.firstName
            binding.tvFamilyCinfo.text = savedCustomer.lastName
            binding.tvEmailCinfo.text = savedCustomer.email
            binding.tvAddressCinfo.text = savedCustomer.billing.address1
            customerInfoViewModel.customerId = savedCustomer.id
            customerInfoViewModel.customer = savedCustomer
        } else {
            binding.llCustomerInfo.visibility = View.GONE
            binding.llNoCustomer.visibility = View.VISIBLE
        }

        val themeColor = customerInfoViewModel.getThemeFromShared(requireActivity())
        binding.switchTheme.isChecked = !(themeColor == null || themeColor == "white")
    }

    private fun setListeners() {
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                customerInfoViewModel.setThemeInSharedPref(requireActivity(), "black")
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                customerInfoViewModel.setThemeInSharedPref(requireActivity(), "white")
            }
        }

        binding.btnNewCustomer.setOnClickListener {
            val action =
                CustomerInfoFragmentDirections.actionCustomerInfoFragmentToCustomerFragment(
                    "",
                    true
                )
            findNavController().navigate(action)
        }

        binding.btnDeleteCustomer.setOnClickListener {
            customerInfoViewModel.deleteCustomer(customerInfoViewModel.customerId)
        }

        binding.btnEditCustomer.setOnClickListener {
            setEditLayoutVisible()
            initEditCustomerLayout()
        }

        binding.btnApplyEditCustomer.setOnClickListener {
            if (customerInfoViewModel.customer != null)
                customerInfoViewModel.editCustomer(
                    customerInfoViewModel.customerId,
                    getEditedValuesFromViews()!!
                )
        }

        binding.btnCancelEditCustomer.setOnClickListener {
            setEditCustomerLayoutToDefault()
            setEditLayoutInvisible()
        }
    }

    private fun observeLiveDatas(view: View) {
        customerInfoViewModel.deleteCustomerResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.loadingView.visibility=View.GONE
                    SharedFunctions.showSnackBar("مشتری با موفقیت حذف شد", view)
                    customerInfoViewModel.deleteCustomerFromShared(requireContext())
                    setValuesAndInitViews()
                    setEditAndCancelButtonEnable()
                }
                is NetworkResult.Error -> {
                    SharedFunctions.showSnackBar(
                        response.message.toString() + " حذف مشتری با خطا مواجه شد ",
                        view
                    )
                    binding.loadingView.visibility=View.GONE
                    setEditAndCancelButtonEnable()
                }
                is NetworkResult.Loading -> {
                    SharedFunctions.showSnackBar("جهت حذف مشتری منتظر بمانید", view)
                    setEditAndCancelButtonDisable()
                    binding.loadingView.visibility=View.VISIBLE
                }

            }
        }

        customerInfoViewModel.editCustomerResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.loadingView.visibility=View.GONE
                    SharedFunctions.showSnackBar(
                        "ویرایش مشتری با موفقیت انجام شد", view
                    )
                    response.data.let { customer ->
                        customerInfoViewModel.setCustomerInSharedPref(requireActivity(), customer!!)
                    }
                    setValuesAndInitViews()
                    setEditLayoutInvisible()
                    setEditAndCancelButtonEnable()
                }
                is NetworkResult.Error -> {
                    binding.loadingView.visibility=View.GONE
                    SharedFunctions.showSnackBar(
                        response.message.toString() + " ویرایش مشتری با خطا مواجه شذ ",
                        view
                    )
                    setEditAndCancelButtonEnable()
                }
                is NetworkResult.Loading -> {
                    binding.loadingView.visibility=View.VISIBLE
                    SharedFunctions.showSnackBar("جهت ویرایش مشتری منتظر بمانید", view)
                    setEditAndCancelButtonDisable()
                }

            }
        }
    }

    private fun getEditedValuesFromViews(): CustomerItem? {
        var updatedCustomer: CustomerItem? = null
        if (areValidInputs()) {
            updatedCustomer = CustomerItem(
                customerInfoViewModel.customerId,
                binding.etEmailCinfo.text.toString(),
                binding.etNameCinfo.text.toString(),
                binding.etFamilyCinfo.text.toString(),
                Billing(binding.etAddressCinfo.text.toString())
            )
        }
        return updatedCustomer
    }

    private fun areValidInputs(): Boolean {
        if (binding.etNameCinfo.text.isNullOrBlank()) {
            binding.etNameCinfo.error = "لطفا فیلد را پر کنید"
            return false
        }
        if (binding.etFamilyCinfo.text.isNullOrBlank()) {
            binding.etFamilyCinfo.error = "لطفا فیلد را پر کنید"
            return false
        }
        if (binding.etAddressCinfo.text.isNullOrBlank()) {
            binding.etAddressCinfo.error = "لطفا فیلد را پر کنید"
            return false
        }
        if (binding.etEmailCinfo.text.isNullOrBlank()) {
            binding.etEmailCinfo.error = "لطفا فیلد را پر کنید"
            return false
        }
        if (!binding.etEmailCinfo.text.matches(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"))) {
            binding.etEmailCinfo.error = "فرم ایمیل صحیح نمی باشد"
            return false
        }
        return true
    }

    private fun setEditCustomerLayoutToDefault() {
        binding.etAddressCinfo.setText("")
        binding.etEmailCinfo.setText("")
        binding.etFamilyCinfo.setText("")
        binding.etNameCinfo.setText("")
    }

    private fun initEditCustomerLayout() {
        val customer = customerInfoViewModel.customer
        binding.etAddressCinfo.setText(customer?.billing?.address1)
        binding.etEmailCinfo.setText(customer?.email)
        binding.etFamilyCinfo.setText(customer?.lastName)
        binding.etNameCinfo.setText(customer?.firstName)
    }

    private fun setEditLayoutVisible() {
        binding.llEditCustomer.visibility = View.VISIBLE
        binding.llCustomerInfo.visibility = View.GONE
    }

    private fun setEditLayoutInvisible() {
        binding.llEditCustomer.visibility = View.GONE
        binding.llCustomerInfo.visibility = View.VISIBLE
    }

    private fun setEditAndCancelButtonEnable() {
        binding.btnCancelEditCustomer.isEnabled = true
        binding.btnApplyEditCustomer.isEnabled = true
    }

    private fun setEditAndCancelButtonDisable() {
        binding.btnCancelEditCustomer.isEnabled = false
        binding.btnApplyEditCustomer.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        customerInfoViewModel.deleteCustomerResponse.value=null
        customerInfoViewModel.editCustomerResponse.value=null
    }

}