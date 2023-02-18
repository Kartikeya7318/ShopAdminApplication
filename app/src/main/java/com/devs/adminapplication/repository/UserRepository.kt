package com.devs.adminapplication.repository

import com.devs.adminapplication.models.LoginRequest
import com.devs.adminapplication.models.LoginResponse
import com.devs.adminapplication.retrofit.UserApi
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject

class UserRepository @Inject constructor(private val api: UserApi) {
//    var body:String="{\"userId\":\"amit11\",\"password\":\"test123\"}"
    suspend fun loginUser(@Body loginRequest:LoginRequest): Response<LoginResponse>? {
        return  api.loginUser(loginRequest = loginRequest)
    }
}