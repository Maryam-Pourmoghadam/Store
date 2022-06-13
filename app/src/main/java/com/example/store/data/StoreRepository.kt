package com.example.store.data

import com.example.store.data.network.StoreRemoteDataSource
import com.example.store.model.ProductItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepository @Inject constructor(private val storeRemoteDataSource: StoreRemoteDataSource) {
    suspend fun getNewProducts(): List<ProductItem> {
        return storeRemoteDataSource.getNewProducts()
    }

    suspend fun getProductDetails(id: Int) = storeRemoteDataSource.getProductDetails(id)
    suspend fun getPopularProducts() = storeRemoteDataSource.getPopularProducts()
    suspend fun getBestProducts() = storeRemoteDataSource.getBestProducts()
    suspend fun getCategories() = storeRemoteDataSource.getCategories()
    suspend fun getProductsByCategory(categoryId: Int) =
        storeRemoteDataSource.getProductsByCategory(categoryId)
    suspend fun searchProducts(searchKey:String)=storeRemoteDataSource.searchProducts(searchKey)
}