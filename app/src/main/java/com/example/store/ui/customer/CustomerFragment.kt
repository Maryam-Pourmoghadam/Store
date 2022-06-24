package com.example.store.ui.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentCustomerBinding
import com.example.store.model.CustomerItem
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerFragment : Fragment() {
    lateinit var binding: FragmentCustomerBinding
    val customerViewModel: CustomerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegisterCustomer.setOnClickListener {
            if (areValidInputs()) {
                val customer =
                    CustomerItem(binding.etEmail.text.toString(),
                        binding.etName.text.toString(),
                    binding.etFamily.text.toString())
                customerViewModel.registerCustomer(customer)
                customerViewModel.setCustomerInSharedPref(requireActivity(),customer)
                Snackbar.make(
                    view, "مشتری ذخیره شد",
                    Snackbar.LENGTH_LONG
                ).setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                    .show()
                findNavController().navigate(R.id.action_customerFragment_to_shoppingCartFragment)
            }
        }
    }

    fun areValidInputs(): Boolean {
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
        return true
    }
}