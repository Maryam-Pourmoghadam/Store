package com.example.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetaData(
    @Json(name = "id")
    val id: Int,
    @Json(name = "key")
    val key: String,
    @Json(name = "value")
    val value: String
)