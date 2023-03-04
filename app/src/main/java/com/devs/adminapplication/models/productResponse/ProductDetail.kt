package com.devs.adminapplication.models.productResponse

data class ProductDetail(

    val id: Int,
    val status: Any,
    val color: String,
    val size: String,
    val quantity: Int,
    val price: Double,
    val remaningQuantaty: Int,


    )