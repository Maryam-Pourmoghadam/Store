package com.example.store.ui.customer

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.CustomerItem
import com.example.store.model.ProductOrderItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel()  {

    fun registerCustomer(customer: CustomerItem): CustomerItem ?{
        var returnedCustomer:CustomerItem?=null
        viewModelScope.launch {
            returnedCustomer=storeRepository.registerCustomer(customer)
        }
        return returnedCustomer
    }

    fun setCustomerInSharedPref(activity: Activity,customer:CustomerItem){
        val sharedPref = activity.getSharedPreferences("customer info", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val jsonStr = gson.toJson(customer)
        editor.putString("customer", jsonStr)
        editor.apply()
    }

    fun getCustomerFromSharedPref(activity: Activity):CustomerItem?{
        val sharedPref = activity.getSharedPreferences("customer info", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("customer", "")
        return gson.fromJson(jsonStr, CustomerItem::class.java)
    }
}