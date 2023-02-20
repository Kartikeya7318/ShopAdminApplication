package com.devs.adminapplication.models

data class Product(
    val brand: Brand,
    val id: Int,
    val price: String,
    val productDetails: List<ProductDetail>,
    val productImg: List<ProductImg>,
    val productName: String,
    val status: Any,
    val subCategory: SubCategory
)