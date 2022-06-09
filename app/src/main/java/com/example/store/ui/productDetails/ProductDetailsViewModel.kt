package com.example.store.ui.productDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.Category
import com.example.store.model.ProductItem
import com.example.store.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val productDetails = MutableLiveData<ProductItem>()
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
}