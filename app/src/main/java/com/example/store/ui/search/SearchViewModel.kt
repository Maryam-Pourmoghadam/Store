package com.example.store.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.data.network.NetworkResult
import com.example.store.model.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val searchProductList = MutableLiveData<NetworkResult<List<ProductItem>>>()

    fun searchProducts(
        searchKey: String, categoryId: String?,
        orderBy: String?,
        order: String?,
        attribute: String?,
        attrTerm: String?
    ) {
        viewModelScope.launch {

            searchProductList.postValue(NetworkResult.Loading())
            searchProductList.postValue(
                storeRepository.searchProducts(
                    searchKey,
                    categoryId,
                    orderBy,
                    order,
                    attribute,
                    attrTerm
                )
            )
        }

    }
}