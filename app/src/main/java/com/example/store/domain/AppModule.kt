package com.example.store.domain

import com.example.store.data.StoreRepository
import com.example.store.data.network.BASE_URL
import com.example.store.data.network.StoreRemoteDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule= module{

    single{
        StoreRepository(get())
    }
    single {
        StoreRemoteDataSource()
    }

    single {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)

            .build()


        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .client(client)
            .build()
        retrofit
    }

    //TODO add viewModels
}