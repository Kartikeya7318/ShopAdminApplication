package com.devs.adminapplication.screens.addProducts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.models.addProduct.ProductAdd
import com.devs.adminapplication.models.addProduct.ProductInfo
import com.devs.adminapplication.network.AdminShopApi
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val api: AdminShopApi) : ViewModel() {

    var nProducts:Int=0
    private var _product = ProductAdd()
    val product get() = _product
     fun setProduct(
        name: String,
        subCategoryId: String,
        brandId: String,

        price:String,
        nProducts: Int
    ) {
         _product=_product.copy(
        name=name,
        subCategoryId=subCategoryId,
        brandId = brandId,

        price=price,
         )
         this.nProducts=nProducts
    }
    fun setProductDetails(productInfoList: List<ProductInfo>){
        _product= _product.copy(
            productInfo=productInfoList
        )
        Log.d("Login Flow", "setProductDetails: "+Gson().toJson(product))
    }
    fun addProductToServer(productInfoList: List<ProductInfo>){
        setProductDetails(productInfoList =productInfoList )
        viewModelScope.launch {
            api.addProductToServer(product)
        }

    }
}