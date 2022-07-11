package com.example.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CouponItem(
    @Json(name = "amount")
    val amount: String,
    @Json(name = "code")
    val code: String,
    /*@Json(name = "date_created")
    val dateCreated: String,
    @Json(name = "date_created_gmt")
    val dateCreatedGmt: String,
    @Json(name = "date_expires")
    val dateExpires: String,
    @Json(name = "date_expires_gmt")
    val dateExpiresGmt: String,
    @Json(name = "date_modified")
    val dateModified: String,
    @Json(name = "date_modified_gmt")
    val dateModifiedGmt: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "discount_type")
    val discountType: String,
    @Json(name = "email_restrictions")
    val emailRestrictions: List<Any>,
    @Json(name = "exclude_sale_items")
    val excludeSaleItems: Boolean,
    @Json(name = "excluded_product_categories")
    val excludedProductCategories: List<Any>,
    @Json(name = "excluded_product_ids")
    val excludedProductIds: List<Any>,
    @Json(name = "free_shipping")
    val freeShipping: Boolean,
    @Json(name = "id")
    val id: Int,
    @Json(name = "individual_use")
    val individualUse: Boolean,
    @Json(name = "limit_usage_to_x_items")
    val limitUsageToXItems: Any,
    @Json(name = "_links")
    val links: Links,
    @Json(name = "maximum_amount")
    val maximumAmount: String,
    @Json(name = "meta_data")
    val metaData: List<Any>,
    @Json(name = "minimum_amount")
    val minimumAmount: String,
    @Json(name = "product_categories")
    val productCategories: List<Any>,
    @Json(name = "product_ids")
    val productIds: List<Any>,
    @Json(name = "status")
    val status: String,
    @Json(name = "usage_count")
    val usageCount: Int,
    @Json(name = "usage_limit")
    val usageLimit: Any,
    @Json(name = "usage_limit_per_user")
    val usageLimitPerUser: Any,
    @Json(name = "used_by")
    val usedBy: List<String>*/
)