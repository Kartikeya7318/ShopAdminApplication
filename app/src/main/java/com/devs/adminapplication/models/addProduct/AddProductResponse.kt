package com.devs.adminapplication.models.addProduct

import com.google.gson.annotations.SerializedName

data class AddProductResponse(
    @SerializedName("token")
    var token :String,
    @SerializedName("userId")
    var userId:String,
    @SerializedName("userEmail")
    var userEmail:String
)