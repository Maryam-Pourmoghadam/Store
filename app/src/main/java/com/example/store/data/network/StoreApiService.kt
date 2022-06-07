package com.example.store.data.network

import com.example.store.model.Products
import retrofit2.http.GET
import retrofit2.http.Query

interface StoreApiService {
    @GET("/products")
    suspend fun getProducts(
        @Query("consumer_key")consumerKey:String,
        @Query("consumer_secret")consumerSecret:String
    ):Products
}