package com.example.store.ui.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.store.R
import com.example.store.databinding.FragmentCustomerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerFragment : Fragment() {
lateinit var binding:FragmentCustomerBinding
val customerViewModel:CustomerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegisterCustomer.setOnClickListener {
            if (areValidInputs()){

            }
        }
    }

    fun areValidInputs():Boolean{
        if (binding.etName.text.isNullOrBlank()){
            binding.etName.error="فیلد را پر کنید"
            return false
        }

        if (binding.etFamily.text.isNullOrBlank()){
            binding.etFamily.error="فیلد را پر کنید"
            return false
        }
        if (binding.etEmail.text.isNullOrBlank()){
            binding.etEmail.error="فیلد را پر کنید"
            return false
        }
        if (!binding.etEmail.text.matches(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$")))
        {
            binding.etEmail.error="فرم ایمیل صحیح نمی باشد"
            return false
        }
        return true
    }
}