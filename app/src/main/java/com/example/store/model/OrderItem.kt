package com.example.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderItem(
    @Json(name = "id")
    val id: Int,
   /* @Json(name = "billing")
    val billing: Billing,*/
    /*@Json(name = "cart_hash")
    val cartHash: String,
    @Json(name = "cart_tax")
    val cartTax: String,

    @Json(name = "created_via")
    val createdVia: String,
    @Json(name = "currency")
    val currency: String,
    @Json(name = "currency_symbol")
    val currencySymbol: String,*/
    @Json(name = "customer_id")
    val customerId: Int,
    /*@Json(name = "customer_ip_address")
    val customerIpAddress: String,*/
    /*@Json(name = "customer_note")
    val customerNote: String,
    @Json(name = "customer_user_agent")
    val customerUserAgent: String,
    @Json(name = "date_completed")
    val dateCompleted: String,
    @Json(name = "date_completed_gmt")
    val dateCompletedGmt: String,
    @Json(name = "date_created")
    val dateCreated: String,
    @Json(name = "date_created_gmt")
    val dateCreatedGmt: String,
    @Json(name = "date_modified")
    val dateModified: String,
    @Json(name = "date_modified_gmt")
    val dateModifiedGmt: String,
    @Json(name = "date_paid")
    val datePaid: String,
    @Json(name = "date_paid_gmt")
    val datePaidGmt: String,
    @Json(name = "discount_tax")
    val discountTax: String,
    @Json(name = "discount_total")
    val discountTotal: String,
    @Json(name = "fee_lines")
    val feeLines: List<Any>,*/

    @Json(name = "line_items")
    val lineItems: List<ProductOrderItem>,
    @Json(name = "coupon_lines")
    val couponLines: List<CouponLines>
    /*@Json(name = "_links")
    val links: Links,
    @Json(name = "meta_data")
    val metaData: List<MetaData>,
    @Json(name = "number")
    val number: String,
    @Json(name = "order_key")
    val orderKey: String,
    @Json(name = "parent_id")
    val parentId: Int,
    @Json(name = "payment_method")
    val paymentMethod: String,
    @Json(name = "payment_method_title")
    val paymentMethodTitle: String,
    @Json(name = "prices_include_tax")
    val pricesIncludeTax: Boolean,
    @Json(name = "refunds")
    val refunds: List<Any>,*/
    /*@Json(name = "shipping")
    val shipping: Shipping,
    @Json(name = "shipping_lines")
    val shippingLines: List<Any>,
    @Json(name = "shipping_tax")
    val shippingTax: String,
    @Json(name = "shipping_total")
    val shippingTotal: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "tax_lines")
    val taxLines: List<Any>,*/
    /*@Json(name = "total")
    val total: String,*/
    /*@Json(name = "total_tax")
    val totalTax: String,
    @Json(name = "transaction_id")
    val transactionId: String,
    @Json(name = "version")
    val version: String*/
)