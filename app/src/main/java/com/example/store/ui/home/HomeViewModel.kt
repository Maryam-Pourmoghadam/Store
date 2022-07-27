package com.example.store.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.data.network.NetworkResult
import com.example.store.model.CategoryItem
import com.example.store.model.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    var newProductList = MutableLiveData<NetworkResult<List<ProductItem>>>()
    val popularProductList = MutableLiveData<NetworkResult<List<ProductItem>>>()
    val bestProductList = MutableLiveData<NetworkResult<List<ProductItem>>>()
    val categoryList = MutableLiveData<NetworkResult<List<CategoryItem>>>()
    val salesProduct = MutableLiveData<NetworkResult<ProductItem>>()

    init {
        getSaleProducts()
        getNewProducts()
        getPopularProducts()
        getBestProducts()
        getCategories()
    }

    fun getNewProducts() {
        viewModelScope.launch {
            newProductList.postValue(NetworkResult.Loading())
            newProductList.postValue(storeRepository.getNewProducts())
        }
    }

    fun getPopularProducts() {
        viewModelScope.launch {
            popularProductList.postValue(NetworkResult.Loading())
            popularProductList.postValue(storeRepository.getPopularProducts())
        }
    }

    fun getBestProducts() {
        viewModelScope.launch {
            bestProductList.postValue(NetworkResult.Loading())
            bestProductList.postValue(storeRepository.getBestProducts())
        }
    }


    fun getCategories() {
        viewModelScope.launch {
            categoryList.postValue(NetworkResult.Loading())
            categoryList.postValue(storeRepository.getCategories())
        }
    }

    fun getSaleProducts() {
        viewModelScope.launch {
            salesProduct.postValue(NetworkResult.Loading())
            salesProduct.postValue(storeRepository.getProductDetails(608))
        }

    }

}