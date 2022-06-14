package com.example.store.data.network

import com.example.store.model.CategoryItem
import com.example.store.model.ProductItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreApiService {
    @GET(BASE_PATH +"products")
    suspend fun getNewProducts(
        @Query("orderby")orderBy:String="date",
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= SECRET_KEY
    ):List<ProductItem>

    @GET(BASE_PATH +"products")
    suspend fun getPopularProducts(
        @Query("per_page")perPage:Int=20,
        @Query("orderby")orderBy:String="popularity",
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= SECRET_KEY,

    ):List<ProductItem>

    @GET(BASE_PATH +"products")
    suspend fun getBestProducts(
        @Query("per_page")perPage:Int=20,
        @Query("orderby")orderBy:String="rating",
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= SECRET_KEY,

    ):List<ProductItem>


    @GET(BASE_PATH+"products/{id}")
    suspend fun getProductDetails(
        @Path("id")id:Int,
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= SECRET_KEY
    ):ProductItem

    @GET(BASE_PATH+"products/categories")
    suspend fun getCategories(
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= SECRET_KEY
    ):List<CategoryItem>

    @GET(BASE_PATH+"products")
    suspend fun getProductsByCategory(
        @Query("category")categoryId:Int,
        @Query("per_page")perPage:Int=20,
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= SECRET_KEY,
    ):List<ProductItem>

    @GET(BASE_PATH+"products")
    suspend fun searchProducts(
        @Query("search")searchKey:String,
        @Query("category")categoryId: String?,
        @Query("orderby")orderBy:String?,
        @Query("order")order:String?,
        @Query("per_page")perPage:Int=20,
        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= SECRET_KEY,
    ):List<ProductItem>


}