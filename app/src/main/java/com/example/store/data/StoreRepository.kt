package com.example.store.data

import com.example.store.data.network.StoreRemoteDataSource
import com.example.store.model.CustomerItem
import com.example.store.model.ProductItem
import com.example.store.model.ReviewItem
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

    suspend fun searchProducts(
        searchKey: String, categotyId: String?,
        orderBy: String?,
        order: String?
    ) = storeRemoteDataSource.searchProducts(searchKey,categotyId,orderBy,order)

    suspend fun getReviews(productId:String)=storeRemoteDataSource.getReviews(productId)
    suspend fun registerCustomer(customer: CustomerItem)=storeRemoteDataSource.registerCustomer(customer)

}