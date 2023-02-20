package com.devs.adminapplication.models

data class ProductDetail(
    val color: String,
    val id: Int,
    val price: Double,
    val quantity: Int,
    val remaningQuantaty: Int,
    val size: String,
    val status: Any
)