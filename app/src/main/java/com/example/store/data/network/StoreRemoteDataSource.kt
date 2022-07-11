package com.example.store.data.network

import com.example.store.model.*
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(private val storeApiService: StoreApiService) {
    suspend fun getNewProducts(): List<ProductItem> {
        return storeApiService.getNewProducts()
    }

    suspend fun getPopularProducts(): List<ProductItem> {
        return storeApiService.getPopularProducts()
    }

    suspend fun getBestProducts(): List<ProductItem> {
        return storeApiService.getBestProducts()
    }

    suspend fun getProductDetails(id: Int): ProductItem {
        return storeApiService.getProductDetails(id)
    }

    suspend fun getCategories(): List<CategoryItem> {
        return storeApiService.getCategories()
    }

    suspend fun getProductsByCategory(categotyId: Int): List<ProductItem> {
        return storeApiService.getProductsByCategory(categotyId)
    }

    suspend fun searchProducts(
        searckKey: String,
        categotyId: String?,
        orderBy: String?,
        order: String?,
        attribute: String?,
        attrTerm: String?
    ): List<ProductItem> {
        return storeApiService.searchProducts(
            searckKey,
            categotyId,
            orderBy,
            order,
            attribute,
            attrTerm
        )
    }

    suspend fun getReviews(productId: String): List<ReviewItem> {
        return storeApiService.getReviews(productId)
    }

    suspend fun sendOrders(order: OrderItem): OrderItem {
        return storeApiService.sendOrders(order)
    }

    suspend fun registerCustomer(customer: CustomerItem): CustomerItem {
        return storeApiService.registerCustomer(customer)
    }

    suspend fun getCoupons():List<CouponItem>{
        return storeApiService.getCoupons()
    }
}