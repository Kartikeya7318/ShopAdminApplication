package com.devs.adminapplication.screens.addCategories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.models.categories.Category
import com.devs.adminapplication.network.AdminShopApi
import com.devs.adminapplication.repository.ProductsRepository
import com.devs.adminapplication.screens.addSubCategories.NewSubCategory
import com.devs.adminapplication.screens.details.DetailScreenState
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

data class CategoryScreenState(
    var categories: List<Category> = emptyList()
)
@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val myPreference: MyPreference,
    private val api: AdminShopApi,
): ViewModel(){
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _failReason = MutableStateFlow(" ")
    val failReason: StateFlow<String> = _failReason
    private val _categoryScreenState= MutableStateFlow(DetailScreenState())
    val categoryScreenState get() = _categoryScreenState.asStateFlow()
    fun getAllCategories() {
        Log.d("reload flow", "getAllCategories: ")
        viewModelScope.launch {
            val categories = repository.getAllCategories(myPreference.getStoredTag())
            Log.d("reload flow", "getAllCategories: "+categories.data)
            if (categories.data!=null) {
                _categoryScreenState.value = _categoryScreenState.value.copy(
                    categories = categories.data!!.categories
                )
            }
        }
    }

    fun newCat(categoryName:String){
        _loading.value=true
        Log.d("okhttp", "newCat: $categoryName")
        viewModelScope.launch {
            try {
                val response=api.newCat(
                    token = myPreference.getStoredTag(),
                    name = categoryName
                )
                if (response.code()==200){
                    _loading.value=false
                    _failReason.value="Success"
                }
            }catch (ex: Exception) {
                _loading.value=false
                _failReason.value=ex.message.toString()
                Log.d("LoginFlow", "failreason: " +ex.message.toString() )
            }

        }
    }

    fun deleteSubCat(id: Int){
        _loading.value=true
        viewModelScope.launch {
            try {
                val response=api.deleteCat(
                    token = myPreference.getStoredTag(),
                    id
                )
                if (response.code()==200){
                    _loading.value=false
                    _failReason.value="Success"
                }
            }catch (ex: Exception) {
                _loading.value=false
                _failReason.value=ex.message.toString()
                Log.d("LoginFlow", "failreason: " +ex.message.toString() )
            }

        }
    }

    fun updateCat(name: String,id: Int){
        _loading.value=true
        viewModelScope.launch {
            try {
                val response=api.editCat(
                    token = myPreference.getStoredTag(),
                    id=id,
                    name = name
                )
                if (response.code()==200){
                    _loading.value=false
                    _failReason.value="Success"
                }
            }catch (ex: Exception) {
                _loading.value=false
                _failReason.value=ex.message.toString()
                Log.d("LoginFlow", "failreason: " +ex.message.toString() )
            }

        }
    }
    fun resetFailReason() {
        _failReason.value = " "
    }

}