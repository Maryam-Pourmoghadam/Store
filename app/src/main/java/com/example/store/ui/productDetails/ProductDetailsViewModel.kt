package com.example.store.ui.productDetails

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.ProductItem
import com.example.store.model.ProductOrderItem
import com.example.store.model.ReviewItem
import com.example.store.model.Status
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject


@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val productDetails = MutableLiveData<ProductItem>()
    val relatedProducts = MutableLiveData<List<ProductItem>>()
    val productReviews = MutableLiveData<List<ReviewItem>>()
    val addReviewBtnIsActive = MutableLiveData(false)
    var productPrice: String = ""
    var productName: String = ""
    var editReviewIsEnable = false
    var editReviewId = -1
    var productId = -1
    var reviewList = listOf<ReviewItem>()
    var status = MutableLiveData<Status>()
    fun getProductDetails(id: Int): ProductItem? {
        var productItem: ProductItem? = null
        viewModelScope.launch {
            try {
                status.value = Status.LOADING
                productDetails.value = storeRepository.getProductDetails(id)
                productItem = productDetails.value
                productPrice = productDetails.value!!.price
                productName = productDetails.value!!.name
                productId = productDetails.value!!.id
                status.value = Status.DONE
            } catch (e: Exception) {
                status.value = Status.ERROR
            }

        }
        return productItem
    }

    fun getRelatedProducts(productDetails: ProductItem) {
        val relatedList = mutableListOf<ProductItem>()
        viewModelScope.launch {
            status.value = Status.LOADING
            try {
                productDetails.relatedIds.forEach { id ->
                    relatedList.add(storeRepository.getProductDetails(id))
                }
                relatedProducts.value = relatedList
                status.value = Status.DONE

            } catch (e: Exception) {
                status.value = Status.ERROR
            }
        }
    }

    fun getProductReviews(productId: String) {
        viewModelScope.launch {
            status.value = Status.LOADING
            try {
                //productReviews.value = storeRepository.getReviews(productId)
                reviewList = storeRepository.getReviews(productId) as MutableList<ReviewItem>
                productReviews.value = reviewList
                status.value = Status.DONE

            } catch (e: Exception) {
                status.value = Status.ERROR
            }
        }
    }

    fun sendReview(reviewItem: ReviewItem, context: Context): ReviewItem? {
        var reviewResponse: ReviewItem? = null
        viewModelScope.launch {
            status.value = Status.LOADING
            Toast.makeText(context, "جهت ثبت نظر منتظر بمانید", Toast.LENGTH_SHORT).show()
            try {
                reviewResponse = storeRepository.sendReview(reviewItem)
                status.value = Status.DONE
                Toast.makeText(context, "نظر شما با موفقیت ارسال شد", Toast.LENGTH_SHORT).show()
                addReviewBtnIsActive.value = false
            } catch (e: Exception) {
                status.value = Status.ERROR
                Toast.makeText(context, "مشکلی رخ داده است مجددا تلاش کنید", Toast.LENGTH_SHORT)
                    .show()

            }
        }
        return reviewResponse
    }

    fun deleteReview(reviewId: Int, context: Context) {
        viewModelScope.launch {
            status.value = Status.LOADING
            try {
                storeRepository.deleteReview(reviewId)
                status.value = Status.DONE
                Toast.makeText(context, "نظر مورد نظر با موفقیت حذف شد", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                status.value = Status.ERROR
            }
        }
    }

    fun editReview(reviewId: Int, review: String, rating: Int, context: Context) {
        viewModelScope.launch {
            status.value = Status.LOADING
            Toast.makeText(context, "جهت ویرایش نظر منتظر بمانید", Toast.LENGTH_SHORT).show()
            try {
                storeRepository.updateReview(reviewId, review, rating)
                status.value = Status.DONE
                Toast.makeText(context, "نظر مورد نظر با موفقیت آپدیت شد", Toast.LENGTH_SHORT)
                    .show()
                addReviewBtnIsActive.value = false
                editReviewIsEnable = false
                editReviewId = -1
            } catch (e: Exception) {
                status.value = Status.ERROR
                Toast.makeText(context, "مشکلی رخ داده است مجددا تلاش کنید", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun setProductInSharedPref(activity: Activity, id: Int) {
        val orders = if (getOrderedProductsFromSharedPref(activity).isNullOrEmpty()) {
            mutableListOf()
        } else {
            getOrderedProductsFromSharedPref(activity) as MutableList<ProductOrderItem>
        }
        val newOrder = ProductOrderItem(productName, productPrice.toDouble(), id, 1, productPrice)
        if (orders.contains(newOrder)) {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                "این محصول قبلا به سبد خرید اضافه شده است",
                Snackbar.LENGTH_SHORT
            ).setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                .show()
        } else {
            orders.add(newOrder)
            Snackbar.make(
                activity.findViewById(android.R.id.content), "محصول به سبد خرید اضافه شد",
                Snackbar.LENGTH_SHORT
            ).setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                .show()
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