package com.example.store.ui.categories

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
class CategoriesViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val specificCategoryProducts = MutableLiveData<NetworkResult<List<ProductItem>>>()
    var categoryName = MutableLiveData<String>()
    var categoryID = -1

    fun findSpecificCategoryProduct(categoryID: Int) {
        this.categoryID = categoryID
        viewModelScope.launch {
            specificCategoryProducts.postValue(NetworkResult.Loading())
            specificCategoryProducts.postValue( storeRepository.getProductsByCategory(categoryID))
        }
    }

    fun findCategoryName(specificCategoryProduct: ProductItem) {
        specificCategoryProduct.categories.forEach { category ->
            if (category.id == categoryID)
                categoryName.value = category.name
        }
    }
}