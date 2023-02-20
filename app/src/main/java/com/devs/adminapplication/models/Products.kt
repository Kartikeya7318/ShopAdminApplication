package com.devs.adminapplication.models

data class Products(
    val errorCode: Any,
    val errorMessage: String,
    val products: List<Product>
)