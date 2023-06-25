package com.devs.adminapplication.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var isLoading: Boolean = true,
    var categories: List<Category> = emptyList(),
    var subCategories: List<SubCategory> = emptyList(),
    var brands: List<Brand> = emptyList(),
    var products: List<Product>? = emptyList(),
//    var allProducts: List<Product>? = emptyList()
)
data class TopBarState(
    val searchText: String = "",
    val isSearchBarVisible: Boolean = false,
    val isSearching: Boolean = false,
    val searchResults: List<Product>? = emptyList(),
    val allProducts: List<Product>? = emptyList(),
    val subCategoriesAdded: List<String> = emptyList()
)

sealed class UserAction {
    object SearchIconClicked : UserAction()
    object CloseIconClicked : UserAction()
    data class TextFieldInput(val text: String) : UserAction()
}
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductsRepository, private val myPreference: MyPreference
) : ViewModel() {
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState get() = _homeScreenState.asStateFlow()
    private var _topBarState = MutableStateFlow(TopBarState())
    val topBarState get() = _topBarState.asStateFlow()
    init {
        getAllCategories()
//        getAllSubCategories()
//        getProducts()

    }

    fun getAllBrands() {
        viewModelScope.launch {
            val brands = repository.getAllBrands(myPreference.getStoredTag())
            Log.d("reload flow", "getAllBrands: " + brands.data)
            if (brands.data != null) {
                _homeScreenState.value = _homeScreenState.value.copy(
                    brands = brands.data!!.brands
                )
            }
        }
    }

    fun updateProductListCategory(productListCategory: String) {
        _homeScreenState.value = _homeScreenState.value.copy(
            productListCategory = productListCategory
        )
        getAllSubCategories()
    }

    fun getProducts(
        productListCategory: String = _homeScreenState.value.productListCategory,
        productListSubCategory: String = _homeScreenState.value.productListSubCategory,
//        onSuccess: (List<Product>?) -> Unit ={}
    ) {

        viewModelScope.launch {
            val res = repository.getProducts(
                token = myPreference.getStoredTag(), subCategoryId = productListSubCategory
            )
            if (res.data?.products?.isEmpty() == true) {
                _homeScreenState.value = _homeScreenState.value.copy(
                    productListCategory = productListCategory,
                    productListSubCategory = productListSubCategory,
                    products = emptyList(),
                    isLoading = false
                )
//                onSuccess(emptyList())
            } else {
                _homeScreenState.value = _homeScreenState.value.copy(
                    productListCategory = productListCategory,
                    productListSubCategory = productListSubCategory,
                    products = res.data?.products,
                    isLoading = false
                )
//                onSuccess(res.data?.products)
            }

        }

    }

    fun getAllProducts(
        productListCategory: String = _homeScreenState.value.productListCategory,
        productListSubCategory: String = _homeScreenState.value.productListSubCategory,
        onSuccess: (List<Product>?) -> Unit = {}
    ) {

        viewModelScope.launch {
            val res = repository.getProducts(
                token = myPreference.getStoredTag(), subCategoryId = productListSubCategory
            )
            if (res.data?.products?.isEmpty() == true) {
                onSuccess(emptyList())
            } else {
                onSuccess(res.data?.products)
            }

        }

    }


    fun getAllCategories() {
        viewModelScope.launch {
            val categories = repository.getAllCategories(myPreference.getStoredTag())
            val finalList = mutableListOf<Category>()
            categories.data?.let { finalList.addAll(it.categories) }
            categories.data?.categories = finalList
            _homeScreenState.value = _homeScreenState.value.copy(
                categories = categories.data!!.categories,
                productListCategory = categories.data!!.categories[0].id.toString()
            )
            getAllSubCategories()
        }
    }

    fun getAllSubCategories() {
        viewModelScope.launch {

            val subcategories = repository.getAllSubCategories(myPreference.getStoredTag())
            Log.d("search subcat tags", "getAllSubCategories: ${subcategories.data?.categories?.size}")
            _homeScreenState.value =
                _homeScreenState.value.copy(subCategories = subcategories.data!!.categories.filter { subCategory ->
                    subCategory.categoryId == _homeScreenState.value.productListCategory.toInt()
                })
            Log.d("search subcat tags", "getAllSubCategories: ${_homeScreenState.value.subCategories.size}")
            val temp = _homeScreenState.value.subCategories
            if (temp.isNotEmpty()) {
                _homeScreenState.value =
                    _homeScreenState.value.copy(productListSubCategory = temp[0].id.toString())
                getProducts()
                getAllBrands()
                Log.d("search subcat tags", "getAllSubCategories: temp not empty ${subcategories.data?.categories?.size}")

                if(_topBarState.value.allProducts?.isEmpty() == true) {
                    for (subcategory in subcategories.data!!.categories) {
                        getAllProducts(productListSubCategory = subcategory.id.toString()) {
                            val temp = _topBarState.value.allProducts?.toMutableList()
                            if (it != null && temp != null) {
                                temp.addAll(it)
//                                Log.d("Search tags", "getAllSubCategories: ${_topBarState.value.allProducts!!.size}")
//                                Log.d("Search tags", "getAllSubCategories: ${it.size}")
                                Log.d("Search tags", "getAllSubCategories: ${it.size}")
                            }
                            val subs = _topBarState.value.subCategoriesAdded.toMutableList()
                            if (!subs.contains(subcategory.id.toString())) {
                                subs.add(subcategory.id.toString())
                                _topBarState.value = _topBarState.value.copy(
                                    allProducts = temp,
                                    subCategoriesAdded = subs
                                )
                                Log.d(
                                    "Search tags",
                                    "getAllSubCategories added to topbar: ${_topBarState.value.allProducts!!.size}"
                                )
                            }
                        }
                    }
                }
//

//                Log.d("Search tags", "getAllSubCategories: ${_topBarState.value.allProducts}")
            } else {
                _homeScreenState.value =
                    _homeScreenState.value.copy(products = emptyList(), isLoading = false)
            }
        }
    }
    fun onAction(userAction: UserAction) {
        when (userAction) {
            UserAction.CloseIconClicked -> {
                _topBarState.value = _topBarState.value.copy(isSearchBarVisible = false)
                getProducts()
            }
            UserAction.SearchIconClicked -> {
                _topBarState.value = _topBarState.value.copy(isSearchBarVisible = true)
            }
            is UserAction.TextFieldInput -> {
                _topBarState.value = _topBarState.value.copy(searchText = userAction.text)
                val filteredProducts = _topBarState.value.allProducts?.filter { product ->
                    product.productName.contains(userAction.text, ignoreCase = true)
                }
                _topBarState.value=_topBarState.value.copy(
                    searchResults = filteredProducts
                )
//                if (filteredProducts != null) {
//                    Log.d("search tags", "onAction: ${filteredProducts.size}")
//                    for ( i in filteredProducts)
//                    Log.d("search tags", "onAction: ${i.id}  ${i.productName}")
//                }
                _homeScreenState.value=_homeScreenState.value.copy(
                    products = filteredProducts
                )
            }
        }
    }

//    fun updateSubCatList(productListCategory: String){
//        viewModelScope.launch {
//            val subcategories = repository.getAllSubCategories(myPreference.getStoredTag())
//
//            _homeScreenState.value =
//                _homeScreenState.value.copy(subCategories = subcategories.data?.categories?.filter { subCategory ->
//                    subCategory.categoryId == productListCategory.toInt()
//                } ?: emptyList())
//
//        }
//    }
}