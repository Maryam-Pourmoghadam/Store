package com.example.store.data.network

const val BASE_URL="https://woocommerce.maktabsharif.ir/"
const val BASE_PATH="wp-json/wc/v3/"
const val CONSUMER_KEY="ck_63f4c52da932ddad1570283b31f3c96c4bd9fd6f"
const val SECRET_KEY="cs_294e7de35430398f323b43c21dd1b29f67b5370b"
val keysHashMap=HashMap<String,String>()

fun getBaseOptions(): Map<String, String> {
    val optionsHashMap = HashMap<String, String>()
    optionsHashMap["consumer_key"] = CONSUMER_KEY
    optionsHashMap["consumer_secret"] = SECRET_KEY
    return optionsHashMap
}
