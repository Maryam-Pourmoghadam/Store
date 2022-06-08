package com.example.store.ui.productDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.ProductItem
import kotlinx.coroutines.launch

class ProductDetailsViewModel(val storeRepository: StoreRepository):ViewModel() {
    val productDetails=MutableLiveData<ProductItem>()

    fun getProductDetails(id:Int){
        viewModelScope.launch {
            productDetails.value=storeRepository.getProductDetails(id)
        }
    }
}