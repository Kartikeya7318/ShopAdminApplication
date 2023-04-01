package com.devs.adminapplication.screens.addProducts

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.models.addProduct.ProductAdd
import com.devs.adminapplication.models.addProduct.ProductInfo
import com.devs.adminapplication.network.AdminShopApi
import com.devs.adminapplication.utils.MyPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val api: AdminShopApi,
    private val myPreference: MyPreference
) : ViewModel() {

    private lateinit var img: File
    var nProducts: Int = 0
    private var _product = ProductAdd()
    val product get() = _product
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _failReason = MutableStateFlow(" ")
    val failReason: StateFlow<String> = _failReason
    fun setProduct(
        name: String,
        subCategoryId: String,
        brandId: String,
        price: String,
        nProducts: Int,
        img: File
    ) {
        _product = _product.copy(
            name = name,
            subCategoryId = if (subCategoryId.isDigitsOnly()) subCategoryId.toInt() else 0,
            brandId = brandId.toInt(),
            price = price,
        )
        this.img = img
        this.nProducts = nProducts
    }

    fun setProductDetails(productInfoList: List<ProductInfo>) {
        _product = _product.copy(
            productInfo = productInfoList
        )
        Log.d("Login Flow", "setProductDetails: " + Gson().toJson(product))
    }

    fun addProductToServer(productInfoList: List<ProductInfo>,home: () -> Unit) {
        _loading.value=true
        setProductDetails(productInfoList = productInfoList)
        val fileRequestBody = img.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart =
            MultipartBody.Part.createFormData("files", img.name, fileRequestBody)

        val newProductAdd = ProductAdd(
            name = "Women\'s Half Sleeved T-Shirt",
            subCategoryId = 18,
            brandId = 4,
            price = "450 - 900",
            productInfo = listOf(
                ProductInfo(
                    size = "XL",
                    color = "BLACK",
                    price = 490,
                    quantity = 10
                )
            )
        )

//        Log.d("Datacheck", "addProductToServer: new product "+ Gson().toJson(newProductAdd))
//        Log.d("Datacheck", "addProductToServer: product "+Gson().toJson(product))
//        Log.d("Datacheck", "addProductToServer: new product "+newProductAdd.toString().toRequestBody("application/json".toMediaTypeOrNull()))
//        Log.d("Datacheck", "addProductToServer: product "+product.toString().toRequestBody("application/json".toMediaTypeOrNull()))
        val requestBody = Gson().toJson(product).toRequestBody("application/json".toMediaTypeOrNull())
        viewModelScope.launch {
            try {
                val response=api.uploadProduct(
                    token = myPreference.getStoredTag(),
                    filePart,
                    requestBody = requestBody
                )
                if (response.code()==200){
                    _loading.value = false
                    home()
                }
            }catch (ex: Exception) {
                _loading.value=false
                _failReason.value = ex.message.toString()
                Log.d("LoginFlow", "failreason: " +ex.message.toString() )
            }

        }
    }



}
