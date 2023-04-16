package com.devs.adminapplication.network


import com.devs.adminapplication.models.addProduct.AddProductResponse
import com.devs.adminapplication.models.addProduct.ProductAdd
import com.devs.adminapplication.models.categories.BrandList
import com.devs.adminapplication.models.categories.CategoryList
import com.devs.adminapplication.models.login.LoginRequest
import com.devs.adminapplication.models.login.LoginResponse
import com.devs.adminapplication.models.productResponse.Products
import com.devs.adminapplication.models.subcategories.SubCategoryList
import com.devs.adminapplication.models.updateProduct.ProductUpdate
import com.devs.adminapplication.screens.orderHistory.OrderHistoryResponse
import com.devs.adminapplication.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*

interface AdminShopApi {
    @POST(Constants.LOGIN_URL)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET(Constants.ALL_PRODUCT_URL)
    suspend fun getAllProducts(
        @Header("Authorization") token: String
    ): Products

    @GET("category/")
    suspend fun getAllCategories(
        @Header("Authorization") token: String,
        @Query("status") status:String ="Active"
    ): CategoryList

    @GET("brand/")
    suspend fun getAllBrands(
        @Header("Authorization") token: String,
        @Query("status") status:String ="Active"
    ): BrandList



    @GET("sub_category")
    suspend fun getAllSubCategories(
        @Header("Authorization") token: String,
        @Query("status") status:String ="Active",
    ): SubCategoryList

    @GET(Constants.SUB_PRODUCT_URL)
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Query("subCategoryId") subCategoryId : Long,
        @Query("brandId") brandId: Long,
        @Query("status") status:String ="ACTIVE",
    ): Products

    @GET("product/{id}")
    suspend fun getProduct(
        @Header("Authorization") token: String,
        @Path("id") productId: Int,
    ): Products


    @POST(Constants.ADD_PRODUCT_URL)
    suspend fun addProductToServer(@Body product : ProductAdd,@Part image: MultipartBody.Part): AddProductResponse



    @POST("product")
    @Multipart
    suspend fun uploadProduct(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("req") requestBody: RequestBody
    ):Response<Retrofit>

    @PUT("product/{id}")
    @Multipart
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("req") product: RequestBody
    ): Response<Retrofit>

    @POST("sub_category/")
    @Multipart
    suspend fun newSubCat(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("req") requestBody: RequestBody
    ):Response<Retrofit>

    @DELETE("sub_category/{id}")
    suspend fun deleteSubCat(
        @Header("Authorization") token: String,
        @Path("id") id: Int,

    ):Response<Retrofit>

    @PUT("sub_category/{id}")
    suspend fun editSubCat(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody,
        @Path("id") id: Int,
    ):Response<Retrofit>

    @PUT("sub_category/{id}/image")
    @Multipart
    suspend fun editSubCatImg(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Path("id") id: Int,
    ):Response<Retrofit>

    @POST("category/")
    @FormUrlEncoded
    suspend fun newCat(
        @Header("Authorization") token: String,
        @Field("catageryName") name: String
    ):Response<Retrofit>

    @DELETE("category/{id}")
    suspend fun deleteCat(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        ):Response<Retrofit>

    @PUT("category/{id}")
    suspend fun editCat(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("catageryName") name: String
    ):Response<Retrofit>

    @POST("brand/{name}")
    suspend fun newBrand(
        @Header("Authorization") token: String,
        @Path("name") name: String
    ):Response<Retrofit>

    @DELETE("brand/{id}")
    suspend fun deleteBrand(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ):Response<Retrofit>


    @PUT("brand/{id}")
    suspend fun editBrand(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("brandName") name: String
    ):Response<Retrofit>

    @PUT("product/{prodid}/image/{imgid}")
    @Multipart
    suspend fun editProductImg(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Path("prodid") prodid: Int,
        @Path("imgid") imgid: Int,
    ):Response<Retrofit>


    @GET("user/orderHistory/admin")
    suspend fun getOrderHistory(
        @Query("from") fromDate: String,
        @Query("to") toDate: String,
        @Header("Authorization") token: String
    ): OrderHistoryResponse
}