package com.devs.adminapplication.models.categories

import com.devs.adminapplication.models.productResponse.Brand

data class CategoryList(
    var categories: List<Category>,
    val errorCode: String,
    val errorMessage: String
)
data class BrandList(
    var brands: List<Brand>,
    val errorCode: String,
    val errorMessage: String
)