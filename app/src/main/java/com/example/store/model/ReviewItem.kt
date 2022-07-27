package com.example.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewItem(
    @Json(name = "reviewer")
    val reviewer: String,
    @Json(name = "date_created")
    val dateCreated: String,
    @Json(name = "rating")
    val rating: Int,
    @Json(name = "review")
    val review: String,
    @Json(name = "product_id")
    val productId: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "reviewer_email")
    val reviewerEmail: String
    /*@Json(name = "date_created_gmt")
    val dateCreatedGmt: String,
    @Json(name = "_links")
    val links: Links,
    */
    /*@Json(name = "reviewer_avatar_urls")
    val reviewerAvatarUrls: ReviewerAvatarUrls,
    @Json(name = "status")
    val status: String,
    @Json(name = "verified")
    val verified: Boolean*/
)