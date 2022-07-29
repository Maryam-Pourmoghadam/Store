package com.example.store.data.network

import com.example.store.model.*
import retrofit2.Response
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(private val storeApiService: StoreApiService) {
    suspend fun getNewProducts(): Response<List<ProductItem>> {
        return storeApiService.getNewProducts()
    }

    suspend fun getPopularProducts(): Response<List<ProductItem>> {
        return storeApiService.getPopularProducts()
    }

    suspend fun getBestProducts():Response< List<ProductItem> >{
        return storeApiService.getBestProducts()
    }

    suspend fun getProductDetails(id: Int): Response<ProductItem> {
        return storeApiService.getProductDetails(id)
    }

    suspend fun getCategories(): Response<List<CategoryItem>> {
        return storeApiService.getCategories()
    }

    suspend fun getProductsByCategory(categotyId: Int):Response< List<ProductItem> >{
        return storeApiService.getProductsByCategory(categotyId)
    }

    suspend fun searchProducts(
        searckKey: String,
        categotyId: String?,
        orderBy: String?,
        order: String?,
        attribute: String?,
        attrTerm: String?
    ): Response<List<ProductItem>>{
        return storeApiService.searchProducts(
            searckKey,
            categotyId,
            orderBy,
            order,
            attribute,
            attrTerm
        )
    }

    suspend fun getReviews(productId: String): Response<List<ReviewItem>> {
        return storeApiService.getReviews(productId)
    }

    suspend fun sendOrders(order: OrderItem): Response<OrderItem> {
        return storeApiService.sendOrders(order)
    }

    suspend fun registerCustomer(customer: CustomerItem): Response<CustomerItem> {
        return storeApiService.registerCustomer(customer)
    }

    suspend fun getCoupons():Response<List<CouponItem>>{
        return storeApiService.getCoupons()
    }

    suspend fun sendReview(reviewItem: ReviewItem):Response<ReviewItem>{
        return storeApiService.sendReview(reviewItem)
    }
    suspend fun deleteReview(reviewId:Int):Response<ReviewItem>{
        return storeApiService.deleteReview(reviewId)
    }
    suspend fun updateReview(reviewId: Int,review:String,rating:Int):Response<ReviewItem>{
        return storeApiService.updateReview(reviewId,review,rating)
    }

    suspend fun deleteCustomer(customerId:Int):Response<CustomerItem>{
        return storeApiService.deleteCustomer(customerId)
    }

    suspend fun editCustomer(customerId:Int,customer: CustomerItem):Response<CustomerItem>{
        return storeApiService.editCustomer(customerId,customer)
    }
}