package com.devs.adminapplication.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.devs.adminapplication.R
import com.devs.adminapplication.models.productResponse.Product
import com.devs.adminapplication.models.util.ChipList
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.screens.componenents.Categories

import com.devs.adminapplication.ui.theme.*
import com.devs.adminapplication.utils.Constants
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
//    menuViewModel: MenuViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()

    val homeScreenState by homeViewModel.homeScreenState.collectAsState()
    val topBarState by homeViewModel.topBarState.collectAsState()

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
        if (!topBarState.isSearchBarVisible) {
            Categories(
                selected = homeScreenState.productListCategory,
                categoriesList = catList
            ) {
                scope.launch {
                    homeViewModel.updateProductListCategory(productListCategory = it)

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
        }
        if (homeScreenState.isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = PrimaryDark)
            }

        } else {
            itembox(homeScreenState.products) { id ->
                navController.navigate(route = AdminScreens.DetailsScreen.name + "/$id" + "/${homeScreenState.productListSubCategory}")
            }
        }


    }

}

@Composable
private fun itembox(productList: List<Product>?, onClick: (String) -> Unit) {

    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {


        if (productList == null || productList.isEmpty()) {
            Text(text = "No Product Available")
        } else {
            val newList = ArrayList(productList)
//            newList.add(productList.get(0))
            Log.d("listupdate", "itembox: " + productList.size)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                itemsIndexed(newList!!) { index, item ->
//            Text("Item at index $index is $item",Modifier.border(1.dp, Color.Blue).width(80.dp).wrapContentSize() )
                    DataCard(newList.get(index)) { id ->
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
    onClick: (String) -> Unit
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
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {

            Surface(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .height(225.dp),
                shape = RectangleShape,
                elevation = 4.dp
            ) {
//                AsyncImage(
//                    painter = rememberAsyncImagePainter(
//                        ImageRequest.Builder(LocalContext.current)
//                            .data(data = product.productImg[0].url).apply(block = fun ImageRequest.Builder.() {
//                                crossfade(true)
//                                transformations()
//                            }).build()
//                    ),
//                    contentDescription = "Product Image"
//                )
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.productImg[0].url)
                        .crossfade(true).build(),
//                    placeholder = painterResource(id = R.drawable.shimmercml),
                    contentDescription = "Product Image",
//                    modifier = Modifier.shimmerEffect()
                    )


            }
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = "Id : " + product.id.toString(), maxLines = 1)
            Text(
                text = "Name : " + product.productName.toString(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(text = "Price : " + product.price.toString(), maxLines = 1)
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}





