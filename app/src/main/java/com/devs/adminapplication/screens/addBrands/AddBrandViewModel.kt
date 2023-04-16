package com.devs.adminapplication.screens.addBrands

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.models.productResponse.Brand
import com.devs.adminapplication.network.AdminShopApi
import com.devs.adminapplication.repository.ProductsRepository
import com.devs.adminapplication.screens.details.DetailScreenState
import com.devs.adminapplication.utils.MyPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

data class AddBrandState(
    var brands: List<Brand> = emptyList()
)

@HiltViewModel
class AddBrandViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val myPreference: MyPreference,
    private val api: AdminShopApi,
):ViewModel(){
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _failReason = MutableStateFlow(" ")
    val failReason: StateFlow<String> = _failReason
    private val _addBrandState= MutableStateFlow(AddBrandState())
    val addBrandState get() = _addBrandState.asStateFlow()

    fun getAllBrands(){
        viewModelScope.launch {
            val brands= repository.getAllBrands(myPreference.getStoredTag())
            Log.d("reload flow", "getAllBrands: "+brands.data)
            if (brands.data!=null){
                _addBrandState.value=_addBrandState.value.copy(
                    brands= brands.data!!.brands
                )
            }
        }
    }

    fun newBrand(brandName:String){
        viewModelScope.launch {
            try {
                val response=api.newBrand(
                    token = myPreference.getStoredTag(),
                    name = brandName
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
    fun deleteBrand(id:Int){
        viewModelScope.launch {
            try {
                val response=api.deleteBrand(
                    token = myPreference.getStoredTag(),
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
    fun updateBrand(name: String,id: Int){

        viewModelScope.launch {
            try {
                val response=api.editBrand(
                    token = myPreference.getStoredTag(),
                    id=id,
                    name = name
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