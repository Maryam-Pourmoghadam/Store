package com.example.store.data.network

import com.example.store.model.ProductItem
import com.example.store.model.Products
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreApiService {
    @GET(BASE_PATH +"products")
    suspend fun getProducts(

        @Query("consumer_key")consumerKey:String= CONSUMER_KEY,
        @Query("consumer_secret")consumerSecret:String= SECRET_KEY
    ):List<ProductItem>
}