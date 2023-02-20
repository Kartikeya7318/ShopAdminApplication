package com.devs.adminapplication.network


import android.util.Log
import com.devs.adminapplication.models.LoginRequest
import com.devs.adminapplication.models.LoginResponse
import com.devs.adminapplication.models.Products
import com.devs.adminapplication.utils.Constants
import retrofit2.Call

import retrofit2.Response
import retrofit2.http.*

interface AdminShopApi {
    @POST(Constants.LOGIN_URL)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET(Constants.ALL_PRODUCT_URL)
    suspend fun getAllProducts(
        @Header("Authorization") token: String
    ): Products

}