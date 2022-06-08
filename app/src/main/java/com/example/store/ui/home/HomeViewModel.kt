package com.example.store.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.Category
import com.example.store.model.ProductItem
import kotlinx.coroutines.launch

class HomeViewModel(val storeRepository: StoreRepository):ViewModel() {
    val productList=MutableLiveData<List<ProductItem>>()
    val categoryList=MutableLiveData<List<Category>>()
    init {
        getProducts()
        getCategories()
    }

    private fun getProducts(){
        viewModelScope.launch {
            productList.value=storeRepository.getProducts()
        }
    }

    private fun getCategories(){
        viewModelScope.launch {
            categoryList.value=storeRepository.getCategories()
        }
    }

}