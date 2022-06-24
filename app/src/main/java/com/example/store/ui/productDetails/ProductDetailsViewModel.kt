package com.example.store.ui.productDetails

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.ProductItem
import com.example.store.model.ProductOrderItem
import com.example.store.model.ReviewItem
import com.example.store.model.Status
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject


@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val productDetails = MutableLiveData<ProductItem>()
    val relatedProducts = MutableLiveData<List<ProductItem>>()
    val productReviews=MutableLiveData<List<ReviewItem>>()
     var productPrice: String=""
     var productName: String=""
    var productId=-1
    var status = MutableLiveData<Status>()
    fun getProductDetails(id: Int) {
        viewModelScope.launch {
            try {
                status.value = Status.LOADING
                productDetails.value = storeRepository.getProductDetails(id)
                productPrice = productDetails.value!!.price
                productName = productDetails.value!!.name
                productId=productDetails.value!!.id
                status.value = Status.DONE
            } catch (e: Exception) {
                status.value = Status.ERROR
            }

        }
    }

    fun getRelatedProducts(productDetails: ProductItem) {
        val relatedList = mutableListOf<ProductItem>()
        viewModelScope.launch {
            status.value = Status.LOADING
            try {
                productDetails.relatedIds.forEach { id ->
                    relatedList.add(storeRepository.getProductDetails(id))
                    /*relatedProducts.value =
                        relatedProducts.value?.plus(storeRepository.getProductDetails(id))*/
                }
                relatedProducts.value = relatedList
                status.value = Status.DONE

            } catch (e: Exception) {
                status.value = Status.ERROR
            }
        }
    }

    fun getProductReviews(productId:String){
        viewModelScope.launch {
            status.value = Status.LOADING
            try {
                productReviews.value=storeRepository.getReviews(productId)
                status.value = Status.DONE

            } catch (e: Exception) {
                status.value = Status.ERROR
            }
        }
    }

    fun setProductInSharedPref(activity: Activity, id: Int) {
        val orders= if (getOrderedProductsFromSharedPref(activity).isNullOrEmpty()){
            mutableListOf()
        }else{
            getOrderedProductsFromSharedPref(activity) as MutableList<ProductOrderItem>
        }

        orders.add( ProductOrderItem(productName,productPrice.toDouble(),id,1,productPrice))
        val sharedPref = activity.getSharedPreferences("ordered products", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val jsonStr = gson.toJson(orders)
        editor.putString("orders", jsonStr)
        editor.apply()
    }

    private fun getOrderedProductsFromSharedPref(activity: Activity):List<ProductOrderItem> ?{
        val sharedPref = activity.getSharedPreferences("ordered products", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("orders", "")
        val type: Type = object : TypeToken<List<ProductOrderItem>>() {}.type
        return gson.fromJson(jsonStr, type)
    }




}