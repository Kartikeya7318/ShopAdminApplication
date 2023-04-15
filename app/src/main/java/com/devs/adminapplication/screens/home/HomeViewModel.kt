package com.devs.adminapplication.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.models.categories.Category
import com.devs.adminapplication.models.productResponse.Brand
import com.devs.adminapplication.models.productResponse.Product
import com.devs.adminapplication.models.subcategories.SubCategory
import com.devs.adminapplication.repository.ProductsRepository
import com.devs.adminapplication.utils.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenState(

    var productListCategory: String = "1",
    var productListSubCategory: String = "15",
    var isLoading: Boolean = false,
    var categories: List<Category> = emptyList(),
    var subCategories: List<SubCategory> = emptyList(),
    var brands: List<Brand> = emptyList(),
    var products: List<Product>? = emptyList(),

    )

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductsRepository, private val myPreference: MyPreference
) : ViewModel() {
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState get() = _homeScreenState.asStateFlow()

    init {
        getAllCategories()
//        getAllSubCategories()
//        getProducts()
    }

    fun getAllBrands(){
        viewModelScope.launch {
            val brands= repository.getAllBrands(myPreference.getStoredTag())
            Log.d("reload flow", "getAllBrands: "+brands.data)
            if (brands.data!=null){
                _homeScreenState.value=_homeScreenState.value.copy(
                    brands= brands.data!!.brands
                )
            }
        }
    }
    fun updateProductListCategory(productListCategory: String){
        _homeScreenState.value = _homeScreenState.value.copy(
            productListCategory = productListCategory
        )
        getAllSubCategories()
    }
    fun getProducts(
        productListCategory: String = _homeScreenState.value.productListCategory,
        productListSubCategory: String = _homeScreenState.value.productListSubCategory
    ) {

        viewModelScope.launch {
            val res = repository.getProducts(
                token = myPreference.getStoredTag(), subCategoryId = productListSubCategory
            )

            _homeScreenState.value = _homeScreenState.value.copy(
                productListCategory = productListCategory,
                productListSubCategory = productListSubCategory,
                products = res.data?.products

            )


        }
    }


    fun getAllCategories() {
        viewModelScope.launch {
            val categories = repository.getAllCategories(myPreference.getStoredTag())
            val finalList = mutableListOf<Category>()
            categories.data?.let { finalList.addAll(it.categories) }
            categories.data?.categories = finalList
            _homeScreenState.value = _homeScreenState.value.copy(
                categories = categories.data!!.categories
            )
            getAllSubCategories()
        }
    }

    fun getAllSubCategories() {
        viewModelScope.launch {

            val subcategories = repository.getAllSubCategories(myPreference.getStoredTag())
            val finalList = mutableListOf<SubCategory>()
            subcategories.data?.let { finalList.addAll(it.categories) }
            subcategories.data?.categories = finalList
            _homeScreenState.value =
                _homeScreenState.value.copy(subCategories = subcategories.data!!.categories.filter { subCategory ->
                    subCategory.categoryId == _homeScreenState.value.productListCategory.toInt()
                })
            _homeScreenState.value=_homeScreenState.value.copy(productListSubCategory = _homeScreenState.value.subCategories[0].id.toString())
            getProducts()
            getAllBrands()
        }
    }

    fun updateSubCatList(productListCategory: String){
        viewModelScope.launch {
            val subcategories = repository.getAllSubCategories(myPreference.getStoredTag())

            _homeScreenState.value =
                _homeScreenState.value.copy(subCategories = subcategories.data?.categories?.filter { subCategory ->
                    subCategory.categoryId == productListCategory.toInt()
                } ?: emptyList())

        }
    }
}