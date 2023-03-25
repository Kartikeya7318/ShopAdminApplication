package com.devs.adminapplication.models.addProduct

import com.google.gson.annotations.SerializedName

data class AddProductResponse(
    @SerializedName("status")
    var status :Int,
    @SerializedName("error")
    var error:String,
//    @SerializedName("userEmail")
//    var userEmail:String
//{
//    "timestamp": "2023-03-24T22:42:52.177+00:00",
//    "status": 400,
//    "error": "Bad Request",
//    "path": "/stockmanagement/api/product/"
//}
)