package com.devs.adminapplication.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devs.adminapplication.models.util.DataOrException
import com.devs.adminapplication.models.productResponse.Product
import com.devs.adminapplication.models.productResponse.Products
import com.devs.adminapplication.models.categories.CategoryList
import com.devs.adminapplication.models.subcategories.SubCategoryList
import com.devs.adminapplication.models.util.ChipList
import com.devs.adminapplication.screens.componenents.Categories
import com.devs.adminapplication.ui.theme.*
import com.devs.adminapplication.utils.Constants
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {
//    Text(text = "Hello !")
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val homeScreenState = produceState<DataOrException<Products, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
//        value = homeViewModel.getAllProductsLocal()
        value=homeViewModel.getAllProducts()
    }.value
    val selectedState by homeViewModel.selectedState.collectAsState()
    val productList = homeScreenState.data?.products?.filter { it.id == 1 }
    Log.d("LoginFlow", "HomeScreen: " + productList.toString())
    Log.d("LoginFlow", "HomeScreen: " + homeScreenState.e.toString())
//    Menu(scaffoldState = scaffoldState, scope = scope, navController = rememberNavController( )) {
//        AdminNavigation(startDestination = AdminScreens.HomeScreen.name, navController = navController as NavHostController)
    val categoryState = produceState<DataOrException<CategoryList, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = homeViewModel.getAllCategories()
    }.value
    val subcategoryState = produceState<DataOrException<SubCategoryList, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = homeViewModel.getAllSubCategories()
    }.value
    val catList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (categoryState.data?.categories?.size ?: -1)) {
        val chipList = ChipList(
            id = categoryState.data?.categories?.get(i)?.id.toString(),
            name =categoryState.data?.categories?.get(i)?.name.toString()
        )
        catList.add(chipList)
        Constants.CATEGORIES=catList
    }
    val subCatList:MutableList<ChipList> = mutableListOf()
    for (i in 0 until (subcategoryState.data?.categories?.size ?: -1)) {
        val chipList = ChipList(
            id = subcategoryState.data?.categories?.get(i)?.id.toString(),
            name =subcategoryState.data?.categories?.get(i)?.name.toString()
        )
        subCatList.add(chipList)

        Constants.SUBCATEGORIES=subCatList
    }

    Column(
        Modifier
            .padding(vertical = 0.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Categories(
            selected = selectedState.productListCategory,
            categoriesList = catList
        ) {
            scope.launch {
                homeViewModel.getProducts(productListCategory = it)
            }

        }
        Categories(
            selected = selectedState.productListSubCategory,
            categoriesList = subCatList
        ) {
            scope.launch {
                homeViewModel.getProducts(productListSubCategory = it)
            }

        }

        itembox(productList)

    }

}

@Composable
private fun itembox(productList: List<Product>?) {
    for (i in 0 until (productList?.size ?: -1)) {
        Column(Modifier.padding(horizontal = 15.dp)) {

            Text(text = productList?.get(i)?.id.toString())
            Text(text = productList?.get(i)?.productName.toString())
            Text(text = productList?.get(i)?.subCategory.toString())
            Text(text = productList?.get(i)?.brand.toString())
            Text(text = productList?.get(i)?.status.toString())
            Text(text = productList?.get(i)?.productDetails.toString())
            Text(text = productList?.get(i)?.productImg.toString())
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun AppBar(onMenuClick: () -> Unit) {
    TopAppBar(
        backgroundColor = PrimaryLight,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.size(28.dp),
                    tint = Color.Black
                )
            }

            Text(
                text = "Shop Admin Application",
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = PrimaryText,
            )
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = "Notification",
                modifier = Modifier.size(28.dp),
                tint = Color.Black
            )
        }


    }
}




