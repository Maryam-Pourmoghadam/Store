package com.example.store.data.network

import com.example.store.model.*
import retrofit2.Response
import retrofit2.http.*

interface StoreApiService {
    @GET(BASE_PATH + "products")
    suspend fun getNewProducts(
        @Query("per_page") perPage: Int = 30,
        @Query("orderby") orderBy: String = "date",
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ):Response< List<ProductItem>>

    @GET(BASE_PATH + "products")
    suspend fun getPopularProducts(
        @Query("per_page") perPage: Int = 30,
        @Query("orderby") orderBy: String = "popularity",
        @QueryMap keys: Map<String, String> = getBaseOptions()

    ): Response<List<ProductItem>>

    @GET(BASE_PATH + "products")
    suspend fun getBestProducts(
        @Query("per_page") perPage: Int = 30,
        @Query("orderby") orderBy: String = "rating",
        @QueryMap keys: Map<String, String> = getBaseOptions()

    ): Response<List<ProductItem>>


    @GET(BASE_PATH + "products/{id}")
    suspend fun getProductDetails(
        @Path("id") id: Int,
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ):Response< ProductItem>

    @GET(BASE_PATH + "products/categories")
    suspend fun getCategories(
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ): Response<List<CategoryItem>>

    @GET(BASE_PATH + "products")
    suspend fun getProductsByCategory(
        @Query("category") categoryId: Int,
        @Query("per_page") perPage: Int = 100,
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ): Response<List<ProductItem>>

    @GET(BASE_PATH + "products")
    suspend fun searchProducts(
        @Query("search") searchKey: String,
        @Query("category") categoryId: String?,
        @Query("orderby") orderBy: String?,
        @Query("order") order: String?,
        @Query("attribute") attribute: String?,
        @Query("attribute_term") attrTerm: String?,
        @Query("per_page") perPage: Int = 100,
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ): Response<List<ProductItem>>

    @GET(BASE_PATH + "products/reviews")
    suspend fun getReviews(
        @Query("product") productId: String,
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ): Response<List<ReviewItem>>

    @GET(BASE_PATH + "coupons")
    suspend fun getCoupons(
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ): Response<List<CouponItem>>

    @POST(BASE_PATH + "customers")
    suspend fun registerCustomer(
        @Body customer: CustomerItem,
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ): Response<CustomerItem>

    @POST(BASE_PATH + "orders")
    suspend fun sendOrders(
        @Body order: OrderItem,
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ): Response<OrderItem>

    @POST(BASE_PATH + "products/reviews")
    suspend fun sendReview(
        @Body review: ReviewItem,
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ):Response<ReviewItem>

    @DELETE(BASE_PATH+"products/reviews/{id}")
    suspend fun deleteReview(
        @Path ("id")reviewId:Int,
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ):Response<ReviewItem>

    @PUT(BASE_PATH+"products/reviews/{id}")
    suspend fun updateReview(
        @Path ("id")reviewId:Int,
        @Query("review")review:String,
        @Query("rating")rating:Int,
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ):Response<ReviewItem>

    @DELETE(BASE_PATH+"customers/{id}")
    suspend fun deleteCustomer(
        @Path("id")customerId:Int,
        @Query("force")force:Boolean=true,
        @QueryMap keys: Map<String, String> = getBaseOptions()
    ):Response<CustomerItem>

    @PUT(BASE_PATH+"customers/{id}")
    suspend fun editCustomer(
    @Path("id")customerId:Int,
    @Body customer: CustomerItem,
    @QueryMap keys: Map<String, String> = getBaseOptions()
    ):Response<CustomerItem>

}