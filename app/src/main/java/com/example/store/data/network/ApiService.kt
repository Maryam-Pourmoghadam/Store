package com.example.store.data.network

const val BASE_URL="https://woocommerce.maktabsharif.ir/"
const val BASE_PATH="wp-json/wc/v3/"
const val CONSUMER_KEY="ck_35f6bcc458eed45f8af8716c18772621ad139e13"
const val SECRET_KEY="cs_710d145f6e04fc53ad917475459e14bcda2c9630"
val keysHashMap=HashMap<String,String>()

fun getBaseOptions(): Map<String, String> {
    val optionsHashMap = HashMap<String, String>()
    optionsHashMap["consumer_key"] = CONSUMER_KEY
    optionsHashMap["consumer_secret"] = SECRET_KEY
    return optionsHashMap
}
