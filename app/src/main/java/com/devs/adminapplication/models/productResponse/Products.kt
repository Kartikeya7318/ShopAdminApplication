package com.devs.adminapplication.models.productResponse

import com.devs.adminapplication.models.productResponse.Product

data class Products(
    val errorCode: Any,
    val errorMessage: String,
    val products: List<Product> = emptyList()
)