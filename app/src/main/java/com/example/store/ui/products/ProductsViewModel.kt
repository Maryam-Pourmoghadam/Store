package com.example.store.ui.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.StoreRepository
import com.example.store.data.network.NetworkResult
import com.example.store.model.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {
    val productList=MutableLiveData<NetworkResult<List<ProductItem>>>()
        var productsType=""
        var productsTypeName=""

    fun getProducts(productType:String){
        viewModelScope.launch {
            when(productType){
                "new"->{
                    productsTypeName="جدیدترین ها"
                    productList.postValue(NetworkResult.Loading())
                    productList.postValue(storeRepository.getNewProducts())
                }
                "popular"->{
                    productsTypeName="پربازدید ترین ها"
                    productList.postValue(NetworkResult.Loading())
                    productList.postValue(storeRepository.getPopularProducts())
                }
                else->{
                    productsTypeName="بهترین ها"
                    productList.postValue(NetworkResult.Loading())
                    productList.postValue(storeRepository.getBestProducts())
                }

            }
        }

    }
}