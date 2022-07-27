package com.example.store.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CouponLines(
    @Json(name = "code")
    val code: String
    /*@Json(name = "id")
    val id: String,*/
    /*@Json(name = "discount")
    val discount: String,
    @Json(name = "discount_tax")
    val discountTax: String,
    @Json(name = "meta_data")
    val metaData: List<Any>*/
) {
}