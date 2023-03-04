package com.devs.adminapplication.network


import com.devs.adminapplication.models.categories.CategoryList
import com.devs.adminapplication.models.login.LoginRequest
import com.devs.adminapplication.models.login.LoginResponse
import com.devs.adminapplication.models.productResponse.Products
import com.devs.adminapplication.models.subcategories.SubCategoryList
import com.devs.adminapplication.utils.Constants

import retrofit2.Response
import retrofit2.http.*

interface AdminShopApi {
    @POST(Constants.LOGIN_URL)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET(Constants.ALL_PRODUCT_URL)
    suspend fun getAllProducts(
        @Header("Authorization") token: String
    ): Products

    @GET(Constants.CATEGORY_URL)
    suspend fun getAllCategories(
        @Header("Authorization") token: String
    ): CategoryList

    @GET(Constants.SUB_CATEGORY_URL)
    suspend fun getAllSubCategories(
        @Header("Authorization") token: String
    ): SubCategoryList




}