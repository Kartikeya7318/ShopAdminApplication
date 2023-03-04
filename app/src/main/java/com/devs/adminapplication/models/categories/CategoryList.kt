package com.devs.adminapplication.models.categories

data class CategoryList(
    var categories: List<Category>,
    val errorCode: String,
    val errorMessage: String
)