package com.example.store.ui.search

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
class SearchViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val status = MutableLiveData<Status>()
    val searchProductList = MutableLiveData<List<ProductItem>>()

    fun searchProducts(
        searchKey: String, categotyId: String?,
        orderBy: String?,
        order: String?,
        attribute: String?,
        attrTerm: String?
    ) {
        viewModelScope.launch {
            status.value = Status.LOADING
            try {
                searchProductList.value =
                    storeRepository.searchProducts(
                        searchKey,
                        categotyId,
                        orderBy,
                        order,
                        attribute,
                        attrTerm
                    )
                status.value = Status.DONE
            } catch (e: Exception) {
                status.value = Status.ERROR
            }
        }

    }
}