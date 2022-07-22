package com.example.store.ui.addAddress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentAddAddressBinding
import com.example.store.model.AddressItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAddressFragment : Fragment() {
    lateinit var binding: FragmentAddAddressBinding
    private val addAddressViewModel: AddAddressViewModel by viewModels()
    var navigatedFromCInfo = false
    var mapLocation = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            navigatedFromCInfo = it.getBoolean("navigatedFromCustomerInfo", false)
            mapLocation = it.getString("address_json", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mapLocation.isNotEmpty())
            binding.etAddressLocation.setText(mapLocation)


        val addressAdapter = AddressListAdapter {
            binding.etAddressName.setText(it.name)
            binding.etAddressLocation.setText(it.address)
        }

        addressAdapter.submitList(addAddressViewModel.getAddressListFromSharedPref(requireActivity()))
        binding.rvAddress.adapter = addressAdapter

        binding.btnGoToMap.setOnClickListener {
            val action = AddAddressFragmentDirections.actionAddAddressFragmentToMapFragment(
                navigatedFromCInfo
            )
            findNavController().navigate(action)
        }

        binding.btnSendAddress.setOnClickListener {
            if (binding.etAddressLocation.text.toString().isNotBlank()) {
                val address = binding.etAddressLocation.text.toString()
                val action =
                    AddAddressFragmentDirections.actionAddAddressFragmentToCustomerFragment(
                        address,
                        navigatedFromCInfo
                    )
                findNavController().navigate(action)
            } else {
                Toast.makeText(
                    requireContext(),
                    "لطفا یک ادرس انتخاب یا وارد کنید",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.btnAddAddressToList.setOnClickListener {
            if (binding.etAddressLocation.text.toString().isBlank()) {
                Toast.makeText(
                    requireContext(),
                    "لطفا یک ادرس انتخاب یا وارد کنید",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val addressItem = AddressItem(
                    binding.etAddressName.text.toString(),
                    binding.etAddressLocation.text.toString()
                )
                addAddressViewModel.setAddressInSharedPref(addressItem, requireActivity())
                val newList = addAddressViewModel.getAddressListFromSharedPref(requireActivity())
                addressAdapter.submitList(newList)
            }
        }
    }


}