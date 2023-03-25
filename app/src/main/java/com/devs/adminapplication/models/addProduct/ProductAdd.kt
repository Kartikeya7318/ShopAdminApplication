package com.devs.adminapplication.models.addProduct

import com.google.gson.annotations.SerializedName

data class ProductAdd(
    @SerializedName("name") var name: String = "",
    @SerializedName("subCategoryId")var subCategoryId: Int = 0,
    @SerializedName("brandId")var brandId: Int = 0,
    @SerializedName("price")var price: String = "",
    @SerializedName("productInfo")var productInfo:List<ProductInfo> =emptyList()

)
