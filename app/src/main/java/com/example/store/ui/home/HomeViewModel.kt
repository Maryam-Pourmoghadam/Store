package com.example.store.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.Category
import com.example.store.model.ProductItem
import com.example.store.model.Status
import kotlinx.coroutines.launch

class HomeViewModel(val storeRepository: StoreRepository):ViewModel() {
    val productList=MutableLiveData<List<ProductItem>>()
    val categoryList=MutableLiveData<List<Category>>()
    var productStatus = MutableLiveData<Status>()
    var categoryStatus = MutableLiveData<Status>()
    init {
        getProducts()
        getCategories()
    }

    private fun getProducts(){
        viewModelScope.launch {
            try {
                productStatus.value=Status.LOADING
                productList.value=storeRepository.getProducts()
                productStatus.value=Status.DONE
            }catch (e:Exception){
                productStatus.value=Status.ERROR
            }

        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            try {
                categoryStatus.value = Status.LOADING
                categoryList.value = storeRepository.getCategories()
                categoryStatus.value = Status.DONE
            }catch (e:Exception){
                categoryStatus.value=Status.ERROR
            }

        }
    }

}