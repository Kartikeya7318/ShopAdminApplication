package com.devs.adminapplication.utils

import com.devs.adminapplication.models.util.ChipList

object Constants {
    const val BASE_URL = "http://103.160.145.42/stockmanagement/api/"      //production ip
//    const val BASE_URL = "http://192.168.29.74:8080/stockmanagement/api/" //home wifi ip
//    const val BASE_URL = "http://192.168.134.13:8080/stockmanagement/api/" //mobile hotspot ip
//    const val BASE_URL = "http://10.10.51.61:8080/stockmanagement/api/" //college ip

    const val ADD_PRODUCT_URL = "product"
    const val LOGIN_URL = "auth/token"
    const val ALL_PRODUCT_URL = "product/1"
    const val SUB_PRODUCT_URL = "product"
    const val POSTS_URL = "posts"
    const val USER_TOKEN = "user_token"
    const val USER_ID="user_id"
    const val PASSWORD="password"
    const val USER_DATA = "user_data"
    const val CATEGORY_URL = "category/1"
    const val SUB_CATEGORY_URL = "sub_category/1"

    var CATEGORIES: MutableList<ChipList> = mutableListOf()
    var SUBCATEGORIES: MutableList<ChipList> = mutableListOf()
    var BRAND: MutableList<ChipList> = mutableListOf(ChipList(id = "4", name = "Hugo"))
    val SIZES: MutableList<ChipList> = mutableListOf(ChipList(id = "4", name = "Hugo"))

    val ORDERHISTORY =
        "{\"errorCode\":\"000000\",\"errorMessage\":\"SUCCESS\",\"orderHistory\":[{\"orderId\":12,\"userId\":\"amit11\",\"totalAmount\":500.0,\"color\":\"BLUE\",\"size\":\"XL\",\"quantity\":9},{\"orderId\":13,\"userId\":\"amit11\",\"totalAmount\":490.0,\"color\":\"RED\",\"size\":\"XL\",\"quantity\":5}]}"


}