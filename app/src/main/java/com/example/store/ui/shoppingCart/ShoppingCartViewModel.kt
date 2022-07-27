package com.example.store.ui.shoppingCart

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.data.network.NetworkResult
import com.example.store.model.CouponItem
import com.example.store.model.CustomerItem
import com.example.store.model.OrderItem
import com.example.store.model.ProductOrderItem
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject


@HiltViewModel
class ShoppingCartViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {

    val orders = MutableLiveData<List<ProductOrderItem>>()
    val couponList = MutableLiveData<NetworkResult<List<CouponItem>>>()
    val orderResponse = MutableLiveData<NetworkResult<OrderItem>>()
    val totalPrice = MutableLiveData(0.0)
    var couponCode = ""

    init {
        viewModelScope.launch {
            couponList.postValue(NetworkResult.Loading())
            couponList.postValue(storeRepository.getCoupons())

        }
    }

    fun getOrderedProductsFromSharedPref(activity: Activity): List<ProductOrderItem>? {
        val sharedPref = activity.getSharedPreferences("ordered products", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("orders", "")
        val type: Type = object : TypeToken<List<ProductOrderItem>>() {}.type
        orders.value = gson.fromJson(jsonStr, type)
        calculateTotalPrice()
        return gson.fromJson(jsonStr, type)
    }

    private fun calculateTotalPrice() {
        var total: Double? = 0.0
        if (orders.value != null) {
            for (item in orders.value!!) {
                total = item.total?.let { total?.plus(it.toDouble()) }
            }
            totalPrice.value = total
        }
    }

    fun modifyOrderList(modifiedOrderId: Int?, count: String): List<ProductOrderItem>? {
        val orderList = orders.value
        if (orderList != null) {
            for (item in orderList) {
                if (item.productId == modifiedOrderId) {
                    item.quantity = count.toInt()
                    item.total = (item.quantity * item.price!!).toString()
                    break
                }
            }
            orders.value = orderList!!
        }

        return orderList
    }

    fun deleteSpecificOrder(orderItem: ProductOrderItem): List<ProductOrderItem> {
        val orderList = orders.value as MutableList
        orderList.remove(orderItem)
        orders.value = orderList
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

    fun getCustomerFromSharedPref(activity: Activity): CustomerItem? {
        val sharedPref = activity.getSharedPreferences("customer info", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("customer", "")
        return gson.fromJson(jsonStr, CustomerItem::class.java)
    }

    fun emptyOrderList(context: Context) {
        context.getSharedPreferences("ordered products", Context.MODE_PRIVATE).edit().clear()
            .apply()
        totalPrice.value = 0.0
        //for testing customer fragment
         //context.getSharedPreferences("customer info", Context.MODE_PRIVATE).edit().clear().apply()

    }

    fun sendOrders(order: OrderItem) {
        viewModelScope.launch {
            orderResponse.postValue(NetworkResult.Loading())
            orderResponse.postValue(storeRepository.sendOrders(order))
            couponCode = ""
        }
    }

    fun applyCoupon(code: String, activity: Activity) {
        if (couponList.value != null) {
            if (totalPrice.value != 0.0 && !couponList.value!!.data.isNullOrEmpty()) {
                for (coupon in couponList.value!!.data!!)
                    if (coupon.code == code) {
                        totalPrice.value =
                            totalPrice.value?.minus(coupon.amount.toDouble() / 100 * totalPrice.value!!)
                        couponCode = code
                        break
                    }


            }
            if (couponCode.isEmpty())
                Snackbar.make(
                    activity.findViewById(android.R.id.content), "کد صحیح نمی باشد",
                    Snackbar.LENGTH_LONG
                ).setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                    .show()
        }

    }


}