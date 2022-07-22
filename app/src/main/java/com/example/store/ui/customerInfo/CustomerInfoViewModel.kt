package com.example.store.ui.customerInfo

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.store.data.StoreRepository
import com.example.store.model.CustomerItem
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class CustomerInfoViewModel@Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel()   {

   /* fun setCustomerInSharedPref(activity: Activity, customer: CustomerItem){
        val sharedPref = activity.getSharedPreferences("customer info", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val jsonStr = gson.toJson(customer)
        editor.putString("customer", jsonStr)
        editor.apply()
    }*/

    fun getCustomerFromSharedPref(activity: Activity): CustomerItem?{
        val sharedPref = activity.getSharedPreferences("customer info", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("customer", "")
        return gson.fromJson(jsonStr, CustomerItem::class.java)
    }
}