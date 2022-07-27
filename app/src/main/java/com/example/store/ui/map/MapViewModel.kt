package com.example.store.ui.map

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() :ViewModel() {
    var navigatedFromCInfo=false
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