package com.devs.adminapplication.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.devs.adminapplication.models.productResponse.Product
import com.devs.adminapplication.models.util.ChipList
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.screens.componenents.Categories
import com.devs.adminapplication.ui.theme.*
import com.devs.adminapplication.utils.Constants
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val scope = rememberCoroutineScope()

    val homeScreenState by homeViewModel.homeScreenState.collectAsState()


//    Log.d("StateHoistCheck", "HomeScreen: Loading : " + homeScreenState.isLoading)
//    Log.d("StateHoistCheck", "HomeScreen: Products : " + homeScreenState.products.toString())
//    Log.d("StateHoistCheck", "HomeScreen: Categories : " + homeScreenState.categories.toString())
//    Log.d(
//        "StateHoistCheck",
//        "HomeScreen: SubCategories : " + homeScreenState.subCategories.toString()
//    )
//    Log.d("StateHoistCheck", "HomeScreen: SelecedCat : " + homeScreenState.productListCategory)
//    Log.d("StateHoistCheck", "HomeScreen: SelectedSub : " + homeScreenState.productListSubCategory)

    val catList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (homeScreenState.categories.size)) {
        val chipList = ChipList(
            id = homeScreenState.categories[i].id.toString(),
            name = homeScreenState.categories[i].name.toString()
        )
        catList.add(chipList)
        Constants.CATEGORIES = catList
    }
    val subCatList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (homeScreenState.subCategories.size)) {
        val chipList = ChipList(
            id = homeScreenState.subCategories[i].id.toString(),
            name = homeScreenState.subCategories[i].name.toString()
        )
        subCatList.add(chipList)

        Constants.SUBCATEGORIES = subCatList
    }

    Column(
        Modifier
            .padding(vertical = 0.dp)
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

        itembox(homeScreenState.products){id->
            navController.navigate(route = AdminScreens.DetailsScreen.name+ "/$id")
        }

    }

}

@Composable
private fun itembox(productList: List<Product>?,onClick: (String) -> Unit) {

    Column(modifier = Modifier.padding(start=10.dp,end = 10.dp)) {


        if (productList == null || productList.isEmpty()) {
            Text(text = "No Product Available")
        } else {
            val newList = ArrayList(productList)
            newList.add(productList.get(0))
            Log.d("listupdate", "itembox: " + productList.size)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                itemsIndexed(newList!!) { index, item ->
//            Text("Item at index $index is $item",Modifier.border(1.dp, Color.Blue).width(80.dp).wrapContentSize() )
                    DataCard(newList.get(index)){ id->
                        onClick(id)
                    }
                }
            }
        }
    }


}

@Composable
private fun DataCard(
    product: Product,
    onClick: (String)-> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth()
//            .height(130.dp)
            .clickable {
                onClick(product.id.toString())
            },
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        elevation = 6.dp
    ) {
        Column() {

            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .height(225.dp),
                shape = RectangleShape,
                elevation = 4.dp
            ) {
                Image(
                    painter = rememberImagePainter(data = product.productImg[0].url,
                        builder = {
                            crossfade(true)
                            transformations()
                        }),
                    contentDescription = "Product Image"
                )


            }
            Text(text = product.id.toString())
            Text(text = product.productName.toString())
        }
    }
}






