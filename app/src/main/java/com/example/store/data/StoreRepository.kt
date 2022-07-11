package com.example.store.data

import com.example.store.data.network.StoreRemoteDataSource
import com.example.store.model.*
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
        order: String?,
        attribute: String?,
        attrTerm: String?
    ) = storeRemoteDataSource.searchProducts(
        searchKey, categotyId, orderBy, order, attribute,
        attrTerm
    )

    suspend fun getReviews(productId: String) = storeRemoteDataSource.getReviews(productId)
    suspend fun registerCustomer(customer: CustomerItem) =
        storeRemoteDataSource.registerCustomer(customer)

    suspend fun sendOrders(order: OrderItem) = storeRemoteDataSource.sendOrders(order)
    suspend fun getCoupons()=storeRemoteDataSource.getCoupons()


}