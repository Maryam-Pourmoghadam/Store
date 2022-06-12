package com.example.store.ui.search

import androidx.lifecycle.ViewModel
import com.example.store.data.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel@Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
}