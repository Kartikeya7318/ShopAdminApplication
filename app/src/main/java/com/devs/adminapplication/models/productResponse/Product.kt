package com.devs.adminapplication.models.productResponse

import com.devs.adminapplication.models.subcategories.SubCategory

data class Product(

    val id: Int,
    val productName: String,
    val subCategory: SubCategory,
    val brand: Brand,
    val price: String,
    val status: Any,
    val productDetails: List<ProductDetail>,
    val productImg: List<ProductImg>,

    )