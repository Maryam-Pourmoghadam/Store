package com.example.store.data

import com.example.store.data.network.StoreRemoteDataSource
import com.example.store.model.ProductItem
import com.example.store.model.Products
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepository @Inject constructor(private val storeRemoteDataSource: StoreRemoteDataSource) {
    suspend fun getProducts():List<ProductItem>{
        return storeRemoteDataSource.getProducts()
    }
    suspend fun getProductDetails(id:Int)=storeRemoteDataSource.getProductDetails(id)
    suspend fun getCategories()=storeRemoteDataSource.getCategories()
}