package com.example.store.ui.addAddress

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.store.data.StoreRepository
import com.example.store.model.Address
import com.example.store.model.AddressItem
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.reflect.Type
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel()   {

    fun getAddressListFromSharedPref(activity: Activity):List<AddressItem>?{
        val sharedPref = activity.getSharedPreferences("address list", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("Addresses", "")
        val type: Type = object : TypeToken<List<AddressItem>>() {}.type
        return gson.fromJson(jsonStr, type)
    }

    fun setAddressInSharedPref(address: AddressItem,activity: Activity){
        val addressList = if (getAddressListFromSharedPref(activity).isNullOrEmpty()) {
            mutableListOf()
        } else {
            getAddressListFromSharedPref(activity) as MutableList<AddressItem>
        }
        var isRepeatitive=false
        addressList.forEach { addressItem ->
            if (addressItem.address==address.address)
                isRepeatitive=true
        }
        if (isRepeatitive){
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                "این آدرس قبلا ثبت شده است",
                Snackbar.LENGTH_SHORT
            ).setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                .show()
        }else{
            addressList.add(address)
            val sharedPref = activity.getSharedPreferences("address list", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val gson = Gson()
            val jsonStr = gson.toJson(addressList)
            editor.putString("Addresses", jsonStr)
            editor.apply()
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                "آدرس جدید ثبت شد",
                Snackbar.LENGTH_SHORT
            ).setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                .show()
        }
    }
}