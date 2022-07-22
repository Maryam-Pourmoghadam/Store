package com.example.store.model

data class Address(var name:String=" ", var address:String,val latitude:String,val longitude:String)
{
    var id=""
    init {
        id=latitude+longitude
    }
}
