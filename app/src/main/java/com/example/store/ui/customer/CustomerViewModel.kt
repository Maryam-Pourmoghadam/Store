package com.example.store.ui.customer

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
class CustomerViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel()  {
    val customerWithId=MutableLiveData<NetworkResult<CustomerItem>>()
    var customer: CustomerItem? = null
    var address: String = ""
    var navigatedFromCInfoFrgmnt = false

    fun registerCustomer(customer: CustomerItem){

        viewModelScope.launch {
            customerWithId.postValue(NetworkResult.Loading())
            customerWithId.postValue(storeRepository.registerCustomer(customer))
        }

    }


    fun setCustomerInSharedPref(activity: Activity,customer:CustomerItem?){
        val sharedPref = activity.getSharedPreferences("customer info", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val jsonStr = gson.toJson(customer)
        editor.putString("customer", jsonStr)
        editor.apply()
    }

    fun setInputDataInShared(activity: Activity,name:String,family:String,email:String){
        val sharedPref = activity.getSharedPreferences("customer input data", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("name",name)
        editor.putString("family",family)
        editor.putString("email",email)
        editor.apply()
    }
    fun clearInputDataSharedPref(context: Context){
        context.getSharedPreferences("customer input data", Context.MODE_PRIVATE).edit().clear().apply()
    }

    fun getCustomerFromSharedPref(activity: Activity):CustomerItem?{
        val sharedPref = activity.getSharedPreferences("customer info", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("customer", "")
        return gson.fromJson(jsonStr, CustomerItem::class.java)
    }
}