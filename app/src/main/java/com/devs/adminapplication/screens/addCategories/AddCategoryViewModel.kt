package com.devs.adminapplication.screens.addCategories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.models.categories.Category
import com.devs.adminapplication.network.AdminShopApi
import com.devs.adminapplication.repository.ProductsRepository
import com.devs.adminapplication.screens.details.DetailScreenState
import com.devs.adminapplication.utils.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
}