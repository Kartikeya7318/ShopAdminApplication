package com.devs.adminapplication.screens.details

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.models.categories.Category
import com.devs.adminapplication.models.productResponse.Product
import com.devs.adminapplication.models.subcategories.SubCategory
import com.devs.adminapplication.models.updateProduct.ProductUpdate
import com.devs.adminapplication.network.AdminShopApi
import com.devs.adminapplication.repository.ProductsRepository
import com.devs.adminapplication.utils.MyPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

data class DetailScreenState(
    var categories: List<Category> = emptyList(),
    var subCategories: List<SubCategory> = emptyList(),
    var product: Product? =null
)
@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val myPreference: MyPreference,
    private val api: AdminShopApi,
): ViewModel(){
    private val _detailScreenState= MutableStateFlow(DetailScreenState())
    val detailScreenState get() = _detailScreenState.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _failReason = MutableStateFlow(" ")
    val failReason: StateFlow<String> = _failReason
    fun updateSubCatList(productListCategory: String){
        viewModelScope.launch {
            val subcategories = repository.getAllSubCategories(myPreference.getStoredTag())
            _detailScreenState.value =
                _detailScreenState.value.copy(subCategories = subcategories.data?.categories?.filter { subCategory ->
                    subCategory.categoryId == productListCategory.toInt()
                } ?: emptyList())
        }
    }
    fun getProduct(productId:String){
        viewModelScope.launch {
            val res= repository.getProduct(
                token = myPreference.getStoredTag(), productId = productId
            ).data?.products
            _detailScreenState.value=_detailScreenState.value.copy(
                product = res?.get(0)
            )

            getAllCategories()
            updateSubCatList(_detailScreenState.value.product?.subCategory?.categoryId.toString())
        }

    }
    fun getAllCategories() {
        Log.d("reload flow", "getAllCategories: ")
        viewModelScope.launch {
            val categories = repository.getAllCategories(myPreference.getStoredTag())
            Log.d("reload flow", "getAllCategories: "+categories.data)
            if (categories.data!=null) {
                _detailScreenState.value = _detailScreenState.value.copy(
                    categories = categories.data!!.categories
                )
            }
        }
    }

    fun updateProductOnServer(product:ProductUpdate,productId: Int){
        val requestBody = Gson().toJson(product).toRequestBody("application/json".toMediaTypeOrNull())
        viewModelScope.launch {
            try {
                val response=api.updateProduct(
                    token = myPreference.getStoredTag(),
                    id = productId,
                    product=requestBody
                )
                if (response.code()==200){
                    _loading.value = false
//                    home()
                    Log.d("Update result", "Success: " +response.code() )
                }
            }catch (ex: Exception) {
                _loading.value=false
                _failReason.value = ex.message.toString()
                Log.d("Update result", "failreason: " +ex.message.toString() )
            }
        }
    }

    fun updateProductImg(prodid: Int, imgid: Int, img: File,onSuccess: ()->Unit){
        _loading.value=true
        val fileRequestBody = img.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart =
            MultipartBody.Part.createFormData("files", img.name, fileRequestBody)
        viewModelScope.launch {
            try {
                val response=api.editProductImg(
                    token = myPreference.getStoredTag(),
                    file = filePart,
                    prodid = prodid,
                    imgid = imgid
                )
                if (response.code()==200){
                    //TODO
                    _failReason.value = "Updated"
                    _loading.value=false
                    onSuccess()

                }
            }catch (ex: Exception) {
                //TODO
                _loading.value=false
                _failReason.value = ex.message.toString()
                Log.d("LoginFlow", "failreason: " +ex.message.toString() )
            }

        }
    }
    fun resetFailReason(){
        _failReason.value=" "
    }


}