package com.example.store.data.network

import com.example.store.model.Category
import com.example.store.model.ProductItem
import com.example.store.model.Products

class StoreRemoteDataSource(val storeApiService: StoreApiService) {
    suspend fun getProducts():List<ProductItem>{
        return storeApiService.getProducts()
    }

    suspend fun getProductDetails(id:Int):ProductItem{
        return storeApiService.getProductDetails(id)
    }

    suspend fun getCategories():List<Category>{
        return storeApiService.getCategories()
    }
}