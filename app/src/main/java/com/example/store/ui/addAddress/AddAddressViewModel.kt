package com.example.store.ui.addAddress

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.store.model.AddressItem
import com.example.store.model.SharedFunctions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.reflect.Type
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor() :
    ViewModel() {

    fun getAddressListFromSharedPref(activity: Activity): List<AddressItem>? {
        val sharedPref = activity.getSharedPreferences("address list", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("Addresses", "")
        val type: Type = object : TypeToken<List<AddressItem>>() {}.type
        return gson.fromJson(jsonStr, type)
    }

    fun setAddressInSharedPref(address: AddressItem, activity: Activity) {
        val addressList = if (getAddressListFromSharedPref(activity).isNullOrEmpty()) {
            mutableListOf()
        } else {
            getAddressListFromSharedPref(activity) as MutableList<AddressItem>
        }
        var isRepeatitive = false
        addressList.forEach { addressItem ->
            if (addressItem.address == address.address)
                isRepeatitive = true
        }
        if (isRepeatitive) {
            SharedFunctions.showSnackBar(
                "این آدرس قبلا ثبت شده است",
                activity.findViewById(android.R.id.content)
            )

        } else {
            addressList.add(address)
            val sharedPref = activity.getSharedPreferences("address list", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val gson = Gson()
            val jsonStr = gson.toJson(addressList)
            editor.putString("Addresses", jsonStr)
            editor.apply()
            SharedFunctions.showSnackBar(
                "آدرس جدید ثبت شد",
                activity.findViewById(android.R.id.content)
            )
        }
    }

    fun deleteAddressFromSharedPref(address: AddressItem,activity: Activity){
        val list=getAddressListFromSharedPref(activity) as MutableList<AddressItem>
        list.remove(address)
        setModifiedListInSharedPref(list,activity)
    }

    private fun setModifiedListInSharedPref(modifiedList:List<AddressItem>,activity: Activity){
        val sharedPref = activity.getSharedPreferences("address list", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val jsonStr = gson.toJson(modifiedList)
        editor.putString("Addresses", jsonStr)
        editor.apply()
    }
}