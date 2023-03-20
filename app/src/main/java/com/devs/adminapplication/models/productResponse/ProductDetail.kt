package com.devs.adminapplication.models.productResponse

data class ProductDetail(

    var id: Int,
    val status: Any,
    var color: String,
    var size: String,
    var quantity: Int,
    var price: Double,
    var remaningQuantaty: Int,


    )