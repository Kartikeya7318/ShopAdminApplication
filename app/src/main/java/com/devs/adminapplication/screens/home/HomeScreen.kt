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
import com.devs.adminapplication.models.productResponse.Product
import com.devs.adminapplication.models.util.ChipList
import com.devs.adminapplication.screens.componenents.Categories
import com.devs.adminapplication.ui.theme.*
import com.devs.adminapplication.utils.Constants
import kotlinx.coroutines.launch
import kotlin.math.log


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val scope = rememberCoroutineScope()

    val homeScreenState by homeViewModel.homeScreenState.collectAsState()


    Log.d("StateHoistCheck", "HomeScreen: Loading : "+homeScreenState.isLoading)
    Log.d("StateHoistCheck", "HomeScreen: Products : "+homeScreenState.products.toString())
    Log.d("StateHoistCheck", "HomeScreen: Categories : "+homeScreenState.categories.toString())
    Log.d("StateHoistCheck", "HomeScreen: SubCategories : "+homeScreenState.subCategories.toString())
    Log.d("StateHoistCheck", "HomeScreen: SelecedCat : "+homeScreenState.productListCategory)
    Log.d("StateHoistCheck", "HomeScreen: SelectedSub : "+homeScreenState.productListSubCategory)
    val catList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (homeScreenState.categories.size)) {
        val chipList = ChipList(
            id = homeScreenState.categories[i].id.toString(),
            name = homeScreenState.categories[i].name.toString()
        )
        catList.add(chipList)
        Constants.CATEGORIES=catList
    }
    val subCatList:MutableList<ChipList> = mutableListOf()
    for (i in 0 until (homeScreenState.subCategories.size)) {
        val chipList = ChipList(
            id = homeScreenState.subCategories[i].id.toString(),
            name = homeScreenState.subCategories[i].name.toString()
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
            selected = homeScreenState.productListCategory,
            categoriesList = catList
        ) {
            scope.launch {
                homeViewModel.getProducts(productListCategory = it)
            }

        }
        Categories(
            selected = homeScreenState.productListSubCategory,
            categoriesList = subCatList
        ) {
            scope.launch {
                homeViewModel.getProducts(productListSubCategory = it)
            }

        }

        itembox(homeScreenState.products)

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

    if (productList == null) {
            Text(text = "No Product Available")
    }

    
}




