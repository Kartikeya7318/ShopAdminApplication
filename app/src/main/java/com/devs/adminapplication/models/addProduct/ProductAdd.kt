package com.devs.adminapplication.models.addProduct

data class ProductAdd(
    var _name: String = "",
    var _subCategoryId: String = "",
    var _brandId: String = "",
    var _quantity: String = "",
    var _price: String = "",
    var _nProducts: Int = 0,
    var productInfo:List<ProductInfo> =emptyList()

)
