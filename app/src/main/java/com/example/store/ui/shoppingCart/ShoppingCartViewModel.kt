package com.example.store.ui.shoppingCart

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.store.data.StoreRepository
import com.example.store.model.CustomerItem
import com.example.store.model.ProductOrderItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.reflect.Type
import javax.inject.Inject


@HiltViewModel
class ShoppingCartViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {

    var orders = MutableLiveData<List<ProductOrderItem>>()
    val totalPrice = MutableLiveData(0.0)

    fun getOrderedProductsFromSharedPref(activity: Activity):List<ProductOrderItem>? {
        val sharedPref = activity.getSharedPreferences("ordered products", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("orders", "")
        val type: Type = object : TypeToken<List<ProductOrderItem>>() {}.type
        orders.value = gson.fromJson(jsonStr, type)
        calculateTotalPrice()
        return  gson.fromJson(jsonStr, type)
    }

    private fun calculateTotalPrice() {
        var total = 0.0
        if (orders.value!=null) {
            for (item in orders.value!!) {
                total = total.plus(item.total.toDouble())
            }
            totalPrice.value = total
        }
    }

    fun modifyOrderList(modifiedOrderId: Int, count: String): List<ProductOrderItem>? {
        val orderList = orders.value
        if (orderList != null) {
            for (item in orderList) {
                if (item.productId == modifiedOrderId) {
                    item.quantity = count.toInt()
                    item.total=(item.quantity*item.price).toString()
                    break
                }
            }

        }
        return orderList
    }

    fun setModifiedListInSharedPref(modifiedList: List<ProductOrderItem>?, activity: Activity) {
        val sharedPref = activity.getSharedPreferences("ordered products", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val jsonStr = gson.toJson(modifiedList)
        editor.putString("orders", jsonStr)
        editor.apply()
    }

    fun getCustomerFromSharedPref(activity: Activity): CustomerItem?{
        val sharedPref = activity.getSharedPreferences("customer info", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("customer", "")
        return gson.fromJson(jsonStr, CustomerItem::class.java)
    }

    fun clearOrderList(context: Context){
        context.getSharedPreferences("ordered products", Context.MODE_PRIVATE).edit().clear().apply()
        totalPrice.value=0.0
        //for testing customer fragment
        context.getSharedPreferences("customer info", Context.MODE_PRIVATE).edit().clear().apply()

    }
}