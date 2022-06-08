package com.example.store.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.ProductItem
import kotlinx.coroutines.launch

class HomeViewModel(val storeRepository: StoreRepository):ViewModel() {
    val productList=MutableLiveData<List<ProductItem>>()
    init {
        getProducts()
    }

    fun getProducts(){
        viewModelScope.launch {
            productList.value=storeRepository.getProducts()
        }

    }

}