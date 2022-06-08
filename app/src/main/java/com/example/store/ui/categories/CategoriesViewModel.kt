package com.example.store.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.ProductItem
import kotlinx.coroutines.launch

class CategoriesViewModel(val storeRepository: StoreRepository) : ViewModel() {
    val specificCategoryProducts = MutableLiveData<List<ProductItem>>()
    var categoryName=""
    fun findSpecificCategoryProduct(categoryID: Int) {
        val resultList = mutableListOf<ProductItem>()
        viewModelScope.launch {
            val productList = storeRepository.getProducts()
            for (product in productList) {
                for (category in product.categories) {
                    if (category.id == categoryID) {
                        categoryName=category.name
                        resultList.add(product)
                        break
                    }
                }
            }
            specificCategoryProducts.value = resultList

        }
    }
}