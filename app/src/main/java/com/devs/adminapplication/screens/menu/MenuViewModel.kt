//package com.devs.adminapplication.screens.menu
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.devs.adminapplication.models.productResponse.Product
//import com.devs.adminapplication.repository.ProductsRepository
//import com.devs.adminapplication.utils.MyPreference
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//data class TopBarState(
//    val searchText: String = "",
//    val isSearchBarVisible: Boolean = false,
//    val isSearching: Boolean = false,
//    val searchResults: List<Product>? = emptyList(),
//    val allProducts: List<Product>? = emptyList()
//)
//
//sealed class UserAction {
//    object SearchIconClicked : UserAction()
//    object CloseIconClicked : UserAction()
//    data class TextFieldInput(val text: String) : UserAction()
//}
//@HiltViewModel
//class MenuViewModel @Inject constructor(
//    private val repository: ProductsRepository, private val myPreference: MyPreference
//) : ViewModel() {
//    init {
//        getAllProducts()
//    }
//    var topBarState by mutableStateOf(TopBarState())
//    fun onAction(userAction: UserAction) {
//        when (userAction) {
//            UserAction.CloseIconClicked -> {
//                topBarState = topBarState.copy(isSearchBarVisible = false)
//            }
//            UserAction.SearchIconClicked -> {
//                topBarState = topBarState.copy(isSearchBarVisible = true)
//            }
//            is UserAction.TextFieldInput -> {
//                topBarState = topBarState.copy(searchText = userAction.text)
//                if (topBarState.allProducts?.isEmpty() == true){
//                    getAllProducts()
//                }
//                val filteredItems = topBarState.allProducts?.filter {
//                    it.productName.contains(userAction.text, ignoreCase = true)
//                }
//                topBarState=topBarState.copy(searchResults = filteredItems)
////                searchJob?.cancel()
////                searchJob = viewModelScope.launch {
////                    if (userAction.text.isNotBlank()) {
////                        topBarState = topBarState.copy(isSearching = true)
////                    }
////                    delay(500L)
////                    searchBooks(userAction.text)
////                }
//            }
//        }
//    }
//    fun getAllProducts(){
//        viewModelScope.launch {
//            val res= repository.getAllProducts(token = myPreference.getStoredTag())
//            if (res.data?.products?.isEmpty()==true){
//                topBarState=topBarState.copy(
//                    allProducts = emptyList()
//                )
//            }else{
//                topBarState=topBarState.copy(
//                    allProducts = res.data?.products
//                )
//            }
//        }
//    }
//}