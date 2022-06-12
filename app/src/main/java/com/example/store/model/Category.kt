package com.example.store.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category (
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name:String,
    @Json(name = "slug")
    val slug: String
)