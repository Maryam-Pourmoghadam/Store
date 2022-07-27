package com.example.store.ui.productDetails

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.data.network.NetworkResult
import com.example.store.model.ProductItem
import com.example.store.model.ProductOrderItem
import com.example.store.model.ReviewItem
import com.example.store.model.SharedFunctions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject


@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val productDetails = MutableLiveData<NetworkResult<ProductItem>>()
    val relatedProducts = MutableLiveData<NetworkResult<List<ProductItem>>>()
    val productReviews = MutableLiveData<NetworkResult<List<ReviewItem>>>()
    val sendReviewResponse = MutableLiveData<NetworkResult<ReviewItem>>()
    val deleteReviewResponse = MutableLiveData<NetworkResult<ReviewItem>>()
    val updateReviewResponse = MutableLiveData<NetworkResult<ReviewItem>>()
    val addReviewBtnIsActive = MutableLiveData(false)
    private var productPrice: String? = ""
    private var productName: String? = ""
    var editReviewIsEnable = false
    var editReviewId = -1
    var productId: Int = -1
    var reviewList: List<ReviewItem>? = listOf()
    fun getProductDetails(): ProductItem? {
        var productItem: ProductItem? = null
        viewModelScope.launch {
            productDetails.postValue(NetworkResult.Loading())
            productDetails.postValue(storeRepository.getProductDetails(productId))
            productItem = productDetails.value?.data
        }
        return productItem
    }

    fun setValues(product: ProductItem) {
        productPrice = product.price
        productName = product.name
    }

    fun getRelatedProducts(productDetails: ProductItem) {
        val relatedList = mutableListOf<ProductItem?>()
        viewModelScope.launch {
            relatedProducts.postValue(NetworkResult.Loading())
            productDetails.relatedIds.forEach { id ->
                relatedList.add(storeRepository.getProductDetails(id).data)
            }
            relatedProducts.postValue(NetworkResult.Success(relatedList as List<ProductItem>))

        }
    }

    fun getProductReviews() {
        viewModelScope.launch {
            productReviews.postValue(NetworkResult.Loading())
            productReviews.postValue(storeRepository.getReviews(productId.toString()))
            reviewList = storeRepository.getReviews(productId.toString()).data
        }
    }

    fun sendReview(reviewItem: ReviewItem) {
        viewModelScope.launch {
            sendReviewResponse.postValue(NetworkResult.Loading())
            sendReviewResponse.postValue(storeRepository.sendReview(reviewItem))
            addReviewBtnIsActive.value = false
        }

    }

    fun deleteReview(reviewId: Int) {
        viewModelScope.launch {
            deleteReviewResponse.postValue(NetworkResult.Loading())
            deleteReviewResponse.postValue(storeRepository.deleteReview(reviewId))


        }
    }

    fun editReview(reviewId: Int, review: String, rating: Int) {
        viewModelScope.launch {
            updateReviewResponse.postValue(NetworkResult.Loading())
            updateReviewResponse.postValue(storeRepository.updateReview(reviewId, review, rating))
            addReviewBtnIsActive.value = false
            editReviewIsEnable = false
            editReviewId = -1

        }
    }

    fun setProductInSharedPref(activity: Activity) {
        val orders = if (getOrderedProductsFromSharedPref(activity).isNullOrEmpty()) {
            mutableListOf()
        } else {
            getOrderedProductsFromSharedPref(activity) as MutableList<ProductOrderItem>
        }
        val newOrder =
            ProductOrderItem(productName, productPrice?.toDouble(), productId, 1, productPrice)
        if (orders.contains(newOrder)) {
            SharedFunctions.showSnackBar(
                "این محصول قبلا به سبد خرید اضافه شده است",
                activity.findViewById(android.R.id.content)
            )
        } else {
            orders.add(newOrder)
            SharedFunctions.showSnackBar("محصول به سبد خرید اضافه شد", activity.findViewById(android.R.id.content))
            val sharedPref = activity.getSharedPreferences("ordered products", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val gson = Gson()
            val jsonStr = gson.toJson(orders)
            editor.putString("orders", jsonStr)
            editor.apply()
        }
    }

    private fun getOrderedProductsFromSharedPref(activity: Activity): List<ProductOrderItem>? {
        val sharedPref = activity.getSharedPreferences("ordered products", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonStr = sharedPref.getString("orders", "")
        val type: Type = object : TypeToken<List<ProductOrderItem>>() {}.type
        return gson.fromJson(jsonStr, type)
    }



}