package com.devs.adminapplication.models.categories

data class Category(
    val id: Int,
    val name: String,
    val product: List<Any>,
    val status: String,
    val subCategory: List<Any>
)