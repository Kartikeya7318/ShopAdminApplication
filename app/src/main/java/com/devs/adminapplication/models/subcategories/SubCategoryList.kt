package com.devs.adminapplication.models.subcategories

data class SubCategoryList(
    var categories: List<SubCategory>,
    val errorCode: String,
    val errorMessage: Any
)