package com.example.store.data.network

import com.example.store.model.*
import retrofit2.http.*

interface StoreApiService {
    @GET(BASE_PATH +"products")
    suspend fun getNewProducts(
        @Query("per_page")perPage:Int=100,
        @Query("orderby")orderBy:String="date",
    @QueryMap keys:Map<String,String> = getBaseOptions()
    ):List<ProductItem>

    @GET(BASE_PATH +"products")
    suspend fun getPopularProducts(
        @Query("per_page")perPage:Int=100,
        @Query("orderby")orderBy:String="popularity",
        @QueryMap keys:Map<String,String> = getBaseOptions()

    ):List<ProductItem>

    @GET(BASE_PATH +"products")
    suspend fun getBestProducts(
        @Query("per_page")perPage:Int=100,
        @Query("orderby")orderBy:String="rating",
        @QueryMap keys:Map<String,String> = getBaseOptions()

    ):List<ProductItem>


    @GET(BASE_PATH+"products/{id}")
    suspend fun getProductDetails(
        @Path("id")id:Int,
        @QueryMap keys:Map<String,String> = getBaseOptions()
    ):ProductItem

    @GET(BASE_PATH+"products/categories")
    suspend fun getCategories(
        @QueryMap keys:Map<String,String> = getBaseOptions()
    ):List<CategoryItem>

    @GET(BASE_PATH+"products")
    suspend fun getProductsByCategory(
        @Query("category")categoryId:Int,
        @Query("per_page")perPage:Int=100,
        @QueryMap keys:Map<String,String> = getBaseOptions()
    ):List<ProductItem>

    @GET(BASE_PATH+"products")
    suspend fun searchProducts(
        @Query("search")searchKey:String,
        @Query("category")categoryId: String?,
        @Query("orderby")orderBy:String?,
        @Query("order")order:String?,
        @Query("per_page")perPage:Int=100,
        @QueryMap keys:Map<String,String> = getBaseOptions()
    ):List<ProductItem>

    @GET(BASE_PATH+"products/reviews")
    suspend fun getReviews(
        @Query("product_id")productId:String,
        @QueryMap keys:Map<String,String> = getBaseOptions()
    ):List<ReviewItem>

    @POST(BASE_PATH+"customers")
    suspend fun registerCustomer(@Body customer:CustomerItem,
        @QueryMap keys:Map<String,String> = getBaseOptions()):CustomerItem

    @POST(BASE_PATH+"orders")
    suspend fun sendOrders(
        @Body order:OrderItem,
        @QueryMap keys:Map<String,String> = getBaseOptions()
    ):OrderItem

}