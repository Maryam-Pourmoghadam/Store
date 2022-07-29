package com.example.store.ui.addAddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.databinding.FragmentAddAddressBinding
import com.example.store.model.AddressItem
import com.example.store.model.SharedFunctions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAddressFragment : Fragment() {
    private lateinit var binding: FragmentAddAddressBinding
    private val addAddressViewModel: AddAddressViewModel by viewModels()
    private var navigatedFromCInfo = false
    private var mapLocation = ""
    private var addressAdapter: AddressListAdapter? = null
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
        setAddressAdapter()
        setButtonsListener(view)

    }


    private fun setAddressAdapter() {
        addressAdapter = AddressListAdapter({
            //onWholeClick
            binding.etAddressName.setText(it.name)
            binding.etAddressLocation.setText(it.address)
        }, {//onDeleteClick
            addAddressViewModel.deleteAddressFromSharedPref(it, requireActivity())
            setChangedDataInAdapter()
        })

        addressAdapter!!.submitList(addAddressViewModel.getAddressListFromSharedPref(requireActivity()))
        binding.rvAddress.adapter = addressAdapter

    }

        private fun setButtonsListener(view: View) {
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
                    SharedFunctions.showSnackBar("لطفا یک ادرس انتخاب یا وارد کنید", view)
                }
            }

            binding.btnAddAddressToList.setOnClickListener {
                if (binding.etAddressLocation.text.toString().isBlank()) {
                    SharedFunctions.showSnackBar("لطفا یک ادرس انتخاب یا وارد کنید", view)
                } else {
                    val addressItem = AddressItem(
                        binding.etAddressName.text.toString(),
                        binding.etAddressLocation.text.toString()
                    )
                    addAddressViewModel.setAddressInSharedPref(addressItem, requireActivity())
                    val newList =
                        addAddressViewModel.getAddressListFromSharedPref(requireActivity())
                    addressAdapter!!.submitList(newList)
                }
            }
        }

    private fun setChangedDataInAdapter(){
        val list=addAddressViewModel.getAddressListFromSharedPref(requireActivity())
        addressAdapter?.submitList(list)
    }

    }

