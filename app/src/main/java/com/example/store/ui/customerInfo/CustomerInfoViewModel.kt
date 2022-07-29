package com.example.store.ui.customerInfo

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.data.network.NetworkResult
import com.example.store.model.CustomerItem
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerInfoViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val deleteCustomerResponse=MutableLiveData<NetworkResult<CustomerItem>>()
    val editCustomerResponse=MutableLiveData<NetworkResult<CustomerItem>>()
    var customerId=-1
    var customer:CustomerItem?=null
    fun deleteCustomer(customerId:Int){
        viewModelScope.launch {
            deleteCustomerResponse.postValue(NetworkResult.Loading())
            deleteCustomerResponse.postValue(storeRepository.deleteCustomer(customerId))
        }

    }

    fun editCustomer(customerId: Int,customer:CustomerItem){
        viewModelScope.launch {
            editCustomerResponse.postValue(NetworkResult.Loading())
            editCustomerResponse.postValue(storeRepository.editCustomer(customerId,customer))
        }
    }

    fun setCustomerInSharedPref(activity: Activity, customer: CustomerItem) {
        val sharedPref = activity.getSharedPreferences("customer info", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val jsonStr = gson.toJson(customer)
        editor.putString("customer", jsonStr)
        editor.apply()
    }

    fun getCustomerFromSharedPref(activity: Activity): CustomerItem? {
        val sharedPref = activity.getSharedPreferences("customer info", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("customer", "")
        return gson.fromJson(jsonStr, CustomerItem::class.java)
    }

    fun deleteCustomerFromShared(context: Context) {
        context.getSharedPreferences("customer info", Context.MODE_PRIVATE).edit().clear().apply()

    }

    fun setThemeInSharedPref(activity: Activity, color: String) {
        val sharedPref = activity.getSharedPreferences("theme", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("color", color)
        editor.apply()
    }

    fun getThemeFromShared(activity: Activity): String? {
        val sharedPref = activity.getSharedPreferences("theme", Context.MODE_PRIVATE)
        return sharedPref.getString("color", null)
    }
}