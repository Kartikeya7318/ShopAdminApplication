package com.devs.adminapplication.screens.addSubCategories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.models.categories.Category
import com.devs.adminapplication.models.productResponse.Product
import com.devs.adminapplication.models.subcategories.SubCategory
import com.devs.adminapplication.network.AdminShopApi
import com.devs.adminapplication.repository.ProductsRepository
import com.devs.adminapplication.screens.details.DetailScreenState
import com.devs.adminapplication.utils.MyPreference
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
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

data class SubCategoryScreenState(
    var categories: List<Category> = emptyList(),
    var subCategories: List<SubCategory> = emptyList(),
)

data class NewSubCategory(
    @SerializedName("categoryId") var categoryId: Int = 0,
    @SerializedName("subCategoryName") var subCategoryName: String = "",
)
@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val myPreference: MyPreference,
    private val api: AdminShopApi,
): ViewModel(){
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _failReason = MutableStateFlow(" ")
    val failReason: StateFlow<String> = _failReason
    private val _subCategoryScreenState= MutableStateFlow(DetailScreenState())
    val subCategoryScreenState get() = _subCategoryScreenState.asStateFlow()
    fun getAllCategories() {
        Log.d("reload flow", "getAllCategories: ")
        viewModelScope.launch {
            val categories = repository.getAllCategories(myPreference.getStoredTag())
            Log.d("reload flow", "getAllCategories: "+categories.data)
            if (categories.data!=null) {
                _subCategoryScreenState.value = _subCategoryScreenState.value.copy(
                    categories = categories.data!!.categories
                )
            }
        }
    }
    fun updateSubCatList(productListCategory: String){
        viewModelScope.launch {
            val subcategories = repository.getAllSubCategories(myPreference.getStoredTag())
            _subCategoryScreenState.value =
                _subCategoryScreenState.value.copy(subCategories = subcategories.data?.categories?.filter { subCategory ->
                    subCategory.categoryId == productListCategory.toInt()
                } ?: emptyList())
        }
    }
    fun updateSubCat(subCategory:NewSubCategory,id: Int){
//        val fileRequestBody = img.asRequestBody("image/*".toMediaTypeOrNull())
//        val filePart =
//            MultipartBody.Part.createFormData("file", img.name, fileRequestBody)
        val requestBody = Gson().toJson(subCategory).toRequestBody("application/json".toMediaTypeOrNull())
        viewModelScope.launch {
            try {
                val response=api.editSubCat(
                    token = myPreference.getStoredTag(),
                    requestBody = requestBody,
                    id=id
                )
                if (response.code()==200){
                    //TODO
                }
            }catch (ex: Exception) {
                //TODO
                Log.d("LoginFlow", "failreason: " +ex.message.toString() )
            }

        }
    }
    fun newSubCat(newSubCat: NewSubCategory,img: File){
        val fileRequestBody = img.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart =
            MultipartBody.Part.createFormData("file", img.name, fileRequestBody)
        val requestBody = Gson().toJson(newSubCat).toRequestBody("application/json".toMediaTypeOrNull())
        viewModelScope.launch {
            try {
                val response = api.newSubCat(
                    token = myPreference.getStoredTag(),
                    filePart,
                    requestBody = requestBody
                )
                if (response.code() == 200) {
                    //TODO
                }
            } catch (ex: Exception) {
                //TODO
                Log.d("LoginFlow", "failreason: " + ex.message.toString())
            }

        }
    }
    fun deleteSubCat(id: Int){
        viewModelScope.launch {
            try {
                val response=api.deleteSubCat(
                    token = myPreference.getStoredTag(),
                    id
                )
                if (response.code()==200){
                    //TODO
                }
            }catch (ex: Exception) {
                //TODO
                Log.d("LoginFlow", "failreason: " +ex.message.toString() )
            }

        }
    }

    fun updateSubCatImg(id:Int,img: File){
        val fileRequestBody = img.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart =
            MultipartBody.Part.createFormData("file", img.name, fileRequestBody)
        viewModelScope.launch {
            try {
                val response=api.editSubCatImg(
                    token = myPreference.getStoredTag(),
                    file = filePart,
                    id = id
                )
                if (response.code()==200){
                    //TODO
                }
            }catch (ex: Exception) {
                //TODO
                Log.d("LoginFlow", "failreason: " +ex.message.toString() )
            }

        }
    }
}