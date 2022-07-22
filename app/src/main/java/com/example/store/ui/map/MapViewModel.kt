package com.example.store.ui.map

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.model.Address
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val storeRepository: StoreRepository):ViewModel() {

    fun getAddressFromLatLang(latitude: String,longitude:String, context: Context): String {
        val geoCoder = Geocoder(context, Locale.getDefault())
        var address=""
        viewModelScope.launch {
            val addressList =
                geoCoder.getFromLocation(latitude.toDouble(),longitude.toDouble(), 1)
            address=addressList[0].locality + " " + addressList[0].getAddressLine(0)
        }

        return  address
    }
}