package com.example.store.ui.categories

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
class CategoriesViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val specificCategoryProducts = MutableLiveData<List<ProductItem>>()
    var status = MutableLiveData<Status>()
    var categoryName = MutableLiveData<String>()
    var categoryID = -1
    fun findSpecificCategoryProduct(categoryID: Int) {
        this.categoryID = categoryID
        val resultList = mutableListOf<ProductItem>()
        viewModelScope.launch {
            status.value = Status.LOADING
            try {
                val productList = storeRepository.getProducts()
                for (product in productList) {
                    for (category in product.categories) {
                        if (category.id == categoryID) {
                            resultList.add(product)
                            break
                        }
                    }
                }
                status.value = Status.DONE
            } catch (e: Exception) {
                status.value = Status.ERROR
            }
            specificCategoryProducts.value = resultList

        }
    }

    fun findCategoryName(specificCategoryProduct: ProductItem) {
        specificCategoryProduct.categories.forEach { category ->
            if (category.id == categoryID)
                categoryName.value = category.name
        }
    }
}