package com.example.store.data

import com.example.store.data.network.BaseRepository
import com.example.store.data.network.NetworkResult
import com.example.store.data.network.StoreRemoteDataSource
import com.example.store.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepository @Inject constructor(private val storeRemoteDataSource: StoreRemoteDataSource) :
    BaseRepository() {
    suspend fun getNewProducts(): NetworkResult<List<ProductItem>> {
        return safeApiCall { storeRemoteDataSource.getNewProducts() }
    }

    suspend fun getProductDetails(id: Int): NetworkResult<ProductItem> =
        safeApiCall { storeRemoteDataSource.getProductDetails(id) }

    suspend fun getPopularProducts(): NetworkResult<List<ProductItem>> =
        safeApiCall { storeRemoteDataSource.getPopularProducts() }

    suspend fun getBestProducts(): NetworkResult<List<ProductItem>> =
        safeApiCall { storeRemoteDataSource.getBestProducts() }

    suspend fun getCategories(): NetworkResult<List<CategoryItem>> =
        safeApiCall { storeRemoteDataSource.getCategories() }

    suspend fun getProductsByCategory(categoryId: Int): NetworkResult<List<ProductItem>> =
        safeApiCall { storeRemoteDataSource.getProductsByCategory(categoryId) }

    suspend fun searchProducts(
        searchKey: String, categotyId: String?,
        orderBy: String?,
        order: String?,
        attribute: String?,
        attrTerm: String?
    ): NetworkResult<List<ProductItem>> =
        safeApiCall {
            storeRemoteDataSource.searchProducts(
                searchKey, categotyId, orderBy, order, attribute,
                attrTerm
            )
        }

    suspend fun getReviews(productId: String): NetworkResult<List<ReviewItem>> =
        safeApiCall { storeRemoteDataSource.getReviews(productId) }

    suspend fun registerCustomer(customer: CustomerItem): NetworkResult<CustomerItem> =
        safeApiCall { storeRemoteDataSource.registerCustomer(customer) }

    suspend fun sendOrders(order: OrderItem): NetworkResult<OrderItem> =
        safeApiCall { storeRemoteDataSource.sendOrders(order) }

    suspend fun getCoupons(): NetworkResult<List<CouponItem>> =
        safeApiCall { storeRemoteDataSource.getCoupons() }

    suspend fun sendReview(reviewItem: ReviewItem): NetworkResult<ReviewItem> =
        safeApiCall { storeRemoteDataSource.sendReview(reviewItem) }

    suspend fun deleteReview(reviewId: Int): NetworkResult<ReviewItem> =
        safeApiCall { storeRemoteDataSource.deleteReview(reviewId) }

    suspend fun updateReview(
        reviewId: Int,
        review: String,
        rating: Int
    ): NetworkResult<ReviewItem> = safeApiCall {
        storeRemoteDataSource.updateReview(reviewId, review, rating)
    }


}