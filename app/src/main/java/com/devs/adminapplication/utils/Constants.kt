package com.devs.adminapplication.utils

import com.devs.adminapplication.models.util.ChipList

object Constants {
    const val BASE_URL = "http://34.201.27.120/stockmanagement/api/"
    const val ADD_PRODUCT_URL= "product"
    const val LOGIN_URL = "auth/token"
    const val ALL_PRODUCT_URL = "product/1"
    const val SUB_PRODUCT_URL = "product"
    const val POSTS_URL = "posts"
    const val USER_TOKEN = "user_token"
    const val USER_DATA = "user_data"
    const val CATEGORY_URL="category/"
    const val SUB_CATEGORY_URL="sub_category/"
    const val JSON_DATA = """{
    "errorCode": null,
    "errorMessage": "000000",
    "products": [
        {
            "id": 1,
            "productName": "Men's Regular Fit Round Neck Half Sleeved T-Shirt",
            "subCategory": {
                "id": 1,
                "name": "T-Shart",
                "status": null,
                "categoryId": 1,
                "url": "https://my-estore.s3.amazonaws.com/CT_1SUB_1.jpg"
            },
            "brand": {
                "id": 1,
                "name": "Jockey"
            },
            "price": "450",
            "status": null,
            "productDetails": [
                {
                    "id": 1,
                    "status": null,
                    "color": "BLACK",
                    "size": "XL",
                    "quantity": 0,
                    "price": 490.0,
                    "remaningQuantaty": 43
                },
                {
                    "id": 2,
                    "status": null,
                    "color": "BLACK",
                    "size": "S",
                    "quantity": 0,
                    "price": 490.0,
                    "remaningQuantaty": 50
                },
                {
                    "id": 3,
                    "status": null,
                    "color": "BLUE",
                    "size": "L",
                    "quantity": 0,
                    "price": 490.0,
                    "remaningQuantaty": 36
                }
            ],
            "productImg": [
                {
                    "id": 1,
                    "url": "https://my-estore.s3.amazonaws.com/PD_3_Men%27s_Regular_Fit_Round_Neck_Half_Sleeved_T-Shirt.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230220T185421Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=AKIA22TIJVMX2NFDM6FW%2F20230220%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=b46ae0285e657d6e36ef298cf03e06e79a28f575b6dd720ddd0ee3161002fe1d",
                    "status": null
                },
                {
                    "id": 2,
                    "url": "https://my-estore.s3.amazonaws.com/PD_3_Men%27s_Regular_Fit_Round_Neck_Half_Sleeved_T-Shirt_2.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230220T185421Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=AKIA22TIJVMX2NFDM6FW%2F20230220%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=036351e7edb5c22f3e8cfd123d5cf16f0c6d3f96cea230117f32c0aa083c8aba",
                    "status": null
                }
            ]
        },
        {
            "id": 2,
            "productName": "T-Shart",
            "subCategory": {
                "id": 1,
                "name": "T-Shart",
                "status": null,
                "categoryId": 1,
                "url": "https://my-estore.s3.amazonaws.com/CT_1SUB_1.jpg"
            },
            "brand": {
                "id": 1,
                "name": "Jockey"
            },
            "price": "460-500",
            "status": null,
            "productDetails": [
                {
                    "id": 4,
                    "status": null,
                    "color": "BLACK",
                    "size": "XL",
                    "quantity": 0,
                    "price": 490.0,
                    "remaningQuantaty": 10
                },
                {
                    "id": 5,
                    "status": null,
                    "color": "RED",
                    "size": "XL",
                    "quantity": 0,
                    "price": 490.0,
                    "remaningQuantaty": 1
                },
                {
                    "id": 6,
                    "status": null,
                    "color": "BLUE",
                    "size": "XL",
                    "quantity": 0,
                    "price": 500.0,
                    "remaningQuantaty": 10
                }
            ],
            "productImg": [
                {
                    "id": 3,
                    "url": "https://my-estore.s3.amazonaws.com/PD_2_T-Shart.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230220T185421Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=AKIA22TIJVMX2NFDM6FW%2F20230220%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=0b88c6385e20a71775a2e7139c85342d7466110054031c3b8f65cd8a63704b30",
                    "status": null
                }
            ]
        },
        {
            "id": 3,
            "productName": "T-Shart TT",
            "subCategory": {
                "id": 1,
                "name": "T-Shart",
                "status": null,
                "categoryId": 1,
                "url": "https://my-estore.s3.amazonaws.com/CT_1SUB_1.jpg"
            },
            "brand": {
                "id": 1,
                "name": "Jockey"
            },
            "price": "460-500",
            "status": null,
            "productDetails": [
                {
                    "id": 7,
                    "status": null,
                    "color": "BLACK",
                    "size": "XL",
                    "quantity": 0,
                    "price": 490.0,
                    "remaningQuantaty": 10
                },
                {
                    "id": 8,
                    "status": null,
                    "color": "RAD",
                    "size": "XL",
                    "quantity": 0,
                    "price": 490.0,
                    "remaningQuantaty": 10
                },
                {
                    "id": 9,
                    "status": null,
                    "color": "BLUE",
                    "size": "XL",
                    "quantity": 0,
                    "price": 500.0,
                    "remaningQuantaty": 10
                }
            ],
            "productImg": [
                {
                    "id": 4,
                    "url": "https://my-estore.s3.amazonaws.com/PD_3_T-Shart%2520TT.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230220T185421Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=AKIA22TIJVMX2NFDM6FW%2F20230220%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=6a333bf2b65022ebd79162fc06407ef02878b4bcd65704f4ce45c30fcd8693e1",
                    "status": null
                }
            ]
        }
    ]
}"""
    var CATEGORIES:MutableList<ChipList> = mutableListOf()
    var SUBCATEGORIES:MutableList<ChipList> = mutableListOf()
    var BRAND:MutableList<ChipList> = mutableListOf(ChipList(id="4", name = "Hugo"))


}