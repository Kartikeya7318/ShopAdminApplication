package com.devs.adminapplication.models.updateProduct

import com.devs.adminapplication.models.addProduct.ProductInfo
import com.google.gson.annotations.SerializedName

data class ProductUpdate(
    @SerializedName("name") var name: String = "",
    @SerializedName("subCategoryId")var subCategoryId: Int = 0,
    @SerializedName("brandId")var brandId: Int = 0,
    @SerializedName("price")var price: String = "",
    @SerializedName("productInfo")var productInfo:List<Any> =emptyList()
)

data class ProductUpdateInfo (
    var color: String="",
    var price: Double=0.0,
    var size: String="",
    var quantity: Int=0
        )
data class ProductUpdateInfoWithKey (
    var id: Int=0,
    var color: String="",
    var price: Double=0.0,
    var size: String="",
    var quantity: Int=0
)

