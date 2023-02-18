package com.devs.adminapplication.retrofit


import RetrofitClient
import com.devs.adminapplication.models.LoginRequest
import com.devs.adminapplication.models.LoginResponse

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {
    @POST(Constants.LOGIN_URL)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

}