package com.devs.adminapplication.screens.addProducts

import androidx.lifecycle.ViewModel
import com.devs.adminapplication.models.addProduct.ProductAdd
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor() : ViewModel() {

    private val _product = ProductAdd()
    val product get() = _product
     fun setProduct(
        name: String,
        subCategoryId: String,
        brandId: String,
        quantity:String,
        price:String,
        nProducts: Int
    ) {
        _product._name=name
        _product._subCategoryId=subCategoryId
        _product._brandId = brandId
        _product._quantity=quantity
        _product._price=price
        _product._nProducts=nProducts
    }
}