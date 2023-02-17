package com.devs.adminapplication.repository

import com.devs.adminapplication.models.LoginRequest
import com.devs.adminapplication.models.LoginResponse
import com.devs.adminapplication.retrofit.UserApi
import retrofit2.Response
import retrofit2.http.Body

class UserRepository {
//    var body:String="{\"userId\":\"amit11\",\"password\":\"test123\"}"
    suspend fun loginUser(@Body loginRequest:LoginRequest): Response<LoginResponse>? {
        return  UserApi.getApi()?.loginUser(loginRequest = loginRequest)
    }
}