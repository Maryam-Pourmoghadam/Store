package com.example.store.ui.customerInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentCustomerInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerInfoFragment : Fragment() {
lateinit var binding:FragmentCustomerInfoBinding
private val customerInfoViewModel:CustomerInfoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentCustomerInfoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        binding.switchTheme.setOnCheckedChangeListener { _, ischecked ->
            if (ischecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.btnNewCustomer.setOnClickListener {
            val action=CustomerInfoFragmentDirections.actionCustomerInfoFragmentToCustomerFragment("",true)
            findNavController().navigate(action)
        }
    }

    private fun initView(){
        val savedCustomer=customerInfoViewModel.getCustomerFromSharedPref(requireActivity())
        if (savedCustomer!=null){
            binding.llCustomerInfo.visibility=View.VISIBLE
            binding.llNoCustomer.visibility=View.GONE
            binding.tvNameCinfo.text=savedCustomer.firstName
            binding.tvFamilyCinfo.text=savedCustomer.lastName
            binding.tvEmailCinfo.text=savedCustomer.email
            binding.tvAddressCinfo.text=savedCustomer.billing.address1
        }else{
            binding.llCustomerInfo.visibility=View.GONE
            binding.llNoCustomer.visibility=View.VISIBLE
        }
    }


}