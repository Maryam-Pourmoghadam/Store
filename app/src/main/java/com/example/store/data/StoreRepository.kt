package com.example.store.data

import com.example.store.data.network.StoreRemoteDataSource
import com.example.store.model.ProductItem
import com.example.store.model.Products

class StoreRepository(val storeRemoteDataSource: StoreRemoteDataSource) {
    suspend fun getProducts():List<ProductItem>{
        return storeRemoteDataSource.getProducts()
    }
    suspend fun getProductDetails(id:Int)=storeRemoteDataSource.getProductDetails(id)
    suspend fun getCategories()=storeRemoteDataSource.getCategories()
}