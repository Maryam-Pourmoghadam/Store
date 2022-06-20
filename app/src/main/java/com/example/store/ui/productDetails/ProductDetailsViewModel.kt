package com.example.store.ui.productDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.ProductItem
import com.example.store.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val productDetails = MutableLiveData<ProductItem>()
    val relatedProducts = MutableLiveData<List<ProductItem>>()
    var status = MutableLiveData<Status>()
    fun getProductDetails(id: Int) {
        viewModelScope.launch {
            try {
                status.value = Status.LOADING
                productDetails.value = storeRepository.getProductDetails(id)
                status.value = Status.DONE
            } catch (e: Exception) {
                status.value = Status.ERROR
            }

        }
    }

    fun getRelatedProducts(productDetails: ProductItem) {
        val relatedList= mutableListOf<ProductItem>()
        viewModelScope.launch {
            status.value = Status.LOADING
            try {
                productDetails.relatedIds.forEach { id ->
                    relatedList.add(storeRepository.getProductDetails(id))
                    /*relatedProducts.value =
                        relatedProducts.value?.plus(storeRepository.getProductDetails(id))*/
                }
                relatedProducts.value=relatedList
                status.value = Status.DONE

            } catch (e: Exception) {
                status.value = Status.ERROR
            }
        }
    }
}