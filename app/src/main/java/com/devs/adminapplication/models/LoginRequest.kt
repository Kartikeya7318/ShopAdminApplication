package com.devs.adminapplication.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("userId")
    var userId: String,

    @SerializedName("password")
    var password: String
)
