package com.devs.adminapplication.models.subcategories

data class SubCategory(

    val id: Int,
    val name: String,
    val status: Any,
    val categoryId: Int,
    val url: String
)