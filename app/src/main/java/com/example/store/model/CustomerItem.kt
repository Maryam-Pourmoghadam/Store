package com.example.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomerItem(
    /*@Json(name = "avatar_url")
    val avatarUrl: String,
    @Json(name = "billing")
    val billing: Billing,
    @Json(name = "date_created")
    val dateCreated: String,
    @Json(name = "date_created_gmt")
    val dateCreatedGmt: String,
    @Json(name = "date_modified")
    val dateModified: String,
    @Json(name = "date_modified_gmt")
    val dateModifiedGmt: String,*/
    @Json(name = "email")
    val email: String,
    @Json(name = "first_name")
    val firstName: String,
    /*@Json(name = "id")
    val id: Int,
    @Json(name = "is_paying_customer")
    val isPayingCustomer: Boolean,*/
    @Json(name = "last_name")
    val lastName: String,
    /*@Json(name = "_links")
    val links: Links,
    @Json(name = "meta_data")
    val metaData: List<MetaData>,
    @Json(name = "role")
    val role: String,
    @Json(name = "shipping")
    val shipping: Shipping,*/
    @Json(name = "username")
    val username: String
)