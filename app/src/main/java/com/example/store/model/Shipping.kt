package com.example.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Shipping(
    @Json(name = "address_1")
    val address1: String,
//    @Json(name = "address_2")
//    val address2: String,
    @Json(name = "city")
    val city: String,
    @Json(name = "company")
    val company: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
   /* @Json(name = "postcode")
    val postcode: String,
    @Json(name = "state")
    val state: String*/
)