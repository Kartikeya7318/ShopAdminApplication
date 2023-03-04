package com.devs.adminapplication.models.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    var token :String,
    @SerializedName("userId")
    var userId:String,
    @SerializedName("userEmail")
    var userEmail:String
)
