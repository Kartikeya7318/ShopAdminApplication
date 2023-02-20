package com.devs.adminapplication.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.devs.adminapplication.models.DataOrException
import com.devs.adminapplication.models.Products
import com.devs.adminapplication.repository.ProductsRepository
import com.devs.adminapplication.utils.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

data class SelectedState(

//    var products: List<Product> = emptyList(),
    var productListCategory: String = "All Categories",
    var productListSubCategory: String = "All Categories"

)

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ProductsRepository,private val myPreference: MyPreference) : ViewModel() {
    private val _selectedState = MutableStateFlow(SelectedState())
    val selectedState get() = _selectedState.asStateFlow()

//    init {
//        _homeScreenState.value = homeScreenState.value.copy(isLoading = false)
//
////        getProducts(productListCategory ="All Categories", productListSubCategory = "All Categories" )
//    }

    suspend fun getProducts(
        productListCategory: String = _selectedState.value.productListCategory,
        productListSubCategory: String = "All Categories"
    ): DataOrException<Products, Boolean, Exception> {
        _selectedState.value = _selectedState.value.copy(
            productListCategory = productListCategory,
            productListSubCategory = productListSubCategory
        )
//        _selectedState.value.productListSubCategory=productListSubCategory
        Log.d("LoginFlow", "getProducts: "+myPreference.getStoredTag())
        return repository.getAllProducts(myPreference.getStoredTag())
    }

    suspend fun getAllProducts(): DataOrException<Products, Boolean, Exception> {
        _selectedState.value = _selectedState.value.copy(
            productListCategory = "All Categories",
            productListSubCategory = "All Categories"
        )
        Log.d("LoginFlow","getAllProducts: "+myPreference.getStoredTag())
        return repository.getAllProducts(myPreference.getStoredTag())
    }
}