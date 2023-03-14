package com.devs.adminapplication.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.devs.adminapplication.models.util.DataOrException
import com.devs.adminapplication.models.productResponse.Products
import com.devs.adminapplication.models.categories.Category
import com.devs.adminapplication.models.categories.CategoryList
import com.devs.adminapplication.models.subcategories.SubCategory
import com.devs.adminapplication.models.subcategories.SubCategoryList
import com.devs.adminapplication.repository.ProductsRepository
import com.devs.adminapplication.utils.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.Exception

data class SelectedState(

//    var products: List<Product> = emptyList(),
    var productListCategory: String = "0",
    var productListSubCategory: String = "All Categories"

)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val myPreference: MyPreference
) : ViewModel() {
    private val _selectedState = MutableStateFlow(SelectedState())
    val selectedState get() = _selectedState.asStateFlow()

//    init {
//        _homeScreenState.value = homeScreenState.value.copy(isLoading = false)
//
////        getProducts(productListCategory ="All Categories", productListSubCategory = "All Categories" )
//    }

    suspend fun getProducts(
        productListCategory: String = _selectedState.value.productListCategory,
        productListSubCategory: String = "0"
    ): DataOrException<Products, Boolean, Exception> {
        _selectedState.value = _selectedState.value.copy(
            productListCategory = productListCategory,
            productListSubCategory = productListSubCategory
        )
//        _selectedState.value.productListSubCategory=productListSubCategory
        Log.d("LoginFlow", "getProducts: " + myPreference.getStoredTag())
        return repository.getAllProducts(myPreference.getStoredTag())
    }

    suspend fun getAllProducts(): DataOrException<Products, Boolean, Exception> {
        _selectedState.value = _selectedState.value.copy(
            productListCategory = "0",
            productListSubCategory = "0"
        )
        Log.d("LoginFlow", "getAllProducts: " + myPreference.getStoredTag())
        return repository.getAllProducts(myPreference.getStoredTag())
    }

    suspend fun getAllProductsLocal(): DataOrException<Products, Boolean, Exception> {
        _selectedState.value = _selectedState.value.copy(
            productListCategory = "0",
            productListSubCategory = "All Categories"
        )
        return repository.getAllProductsLocal()
    }

    suspend fun getAllCategories(): DataOrException<CategoryList, Boolean, Exception> {
        val categories = repository.getAllCategories(myPreference.getStoredTag())
        val category = Category(
            id = 0,
            name = "All Categories",
            product = emptyList(),
            status = "Active",
            subCategory = emptyList()
        )
        val finalList= mutableListOf<Category>()
        finalList.add(category)
        categories.data?.let { finalList.addAll(it.categories) }
        categories.data?.categories = finalList
        return categories
    }

    suspend fun getAllSubCategories(): DataOrException<SubCategoryList, Boolean, Exception> {
        val subcategories = repository.getAllSubCategories(myPreference.getStoredTag())
        val subcategory = SubCategory(
            id = 0,
            name = "All Categories",
            status = "Active",
            categoryId = 0,
            url = ""

        )
        val finalList= mutableListOf<SubCategory>()
        finalList.add(subcategory)
        subcategories.data?.let { finalList.addAll(it.categories) }
        subcategories.data?.categories = finalList
        return subcategories
    }
}