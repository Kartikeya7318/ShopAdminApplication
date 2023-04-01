package com.devs.adminapplication.utils

import com.devs.adminapplication.models.util.ChipList

object Constants {
    const val BASE_URL = "http://34.201.27.120/stockmanagement/api/"
    const val ADD_PRODUCT_URL= "product"
    const val LOGIN_URL = "auth/token"
    const val ALL_PRODUCT_URL = "product/1"
    const val SUB_PRODUCT_URL = "product"
    const val POSTS_URL = "posts"
    const val USER_TOKEN = "user_token"
    const val USER_DATA = "user_data"
    const val CATEGORY_URL="category/"
    const val SUB_CATEGORY_URL="sub_category/"

    var CATEGORIES:MutableList<ChipList> = mutableListOf()
    var SUBCATEGORIES:MutableList<ChipList> = mutableListOf()
    var BRAND:MutableList<ChipList> = mutableListOf(ChipList(id="4", name = "Hugo"))
    val SIZES:MutableList<ChipList> = mutableListOf(ChipList(id="4", name = "Hugo"))


}