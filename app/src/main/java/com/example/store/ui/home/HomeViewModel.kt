package com.example.store.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.CategoryItem
import com.example.store.model.ProductItem
import com.example.store.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val productList = MutableLiveData<List<ProductItem>>()
    val popularProductList = MutableLiveData<List<ProductItem>>()
    val bestProductList = MutableLiveData<List<ProductItem>>()
    val categoryList = MutableLiveData<List<CategoryItem>>()
    var status = MutableLiveData<Status>()

    init {
        getNewProducts()
        getPopularProducts()
        getBestProducts()
        getCategories()
    }

    private fun getNewProducts() {
        viewModelScope.launch {
            try {
                status.value = Status.LOADING
                productList.value = storeRepository.getNewProducts()
                status.value = Status.DONE
            } catch (e: Exception) {
                status.value = Status.ERROR
            }

        }
    }

    private fun getPopularProducts() {
        viewModelScope.launch {
            try {
                status.value = Status.LOADING
                popularProductList.value = storeRepository.getPopularProducts()
                status.value = Status.DONE
            } catch (e: Exception) {
                status.value = Status.ERROR
            }

        }
    }

    private fun getBestProducts() {
        viewModelScope.launch {
            try {
                status.value = Status.LOADING
                bestProductList.value = storeRepository.getBestProducts()
                status.value = Status.DONE
            } catch (e: Exception) {
                status.value = Status.ERROR
            }

        }
    }


    private fun getCategories() {
        viewModelScope.launch {
            try {
                status.value = Status.LOADING
                categoryList.value = storeRepository.getCategories()
                status.value = Status.DONE
            } catch (e: Exception) {
                status.value = Status.ERROR
            }

        }
    }

}