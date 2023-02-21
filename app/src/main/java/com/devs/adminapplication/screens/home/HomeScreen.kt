package com.devs.adminapplication.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
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
import com.devs.adminapplication.data.Categorylists
import com.devs.adminapplication.models.DataOrException
import com.devs.adminapplication.models.Products
import com.devs.adminapplication.screens.componenents.Categories
import com.devs.adminapplication.ui.theme.*
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {
//    Text(text = "Hello !")
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val homeScreenState =produceState<DataOrException<Products, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = homeViewModel.getAllProducts()
    }.value
   val selectedState by homeViewModel.selectedState.collectAsState()
    Log.d("LoginFlow", "HomeScreen: "+ homeScreenState.data?.products.toString())
    Log.d("LoginFlow", "HomeScreen: "+ homeScreenState.e.toString())
//    Menu(scaffoldState = scaffoldState, scope = scope, navController = rememberNavController( )) {
//        AdminNavigation(startDestination = AdminScreens.HomeScreen.name, navController = navController as NavHostController)

        Column(Modifier.padding(vertical = 0.dp)) {

            Categories(
                selected = selectedState.productListCategory,
                categoriesList = Categorylists.getCategories()
            ) {
                scope.launch {
                    homeViewModel.getProducts(productListCategory = it)
                }

            }
            Categories(
                selected = selectedState.productListSubCategory,
                categoriesList = Categorylists.getSubCategories()
            ) {
                scope.launch {
                    homeViewModel.getProducts(productListSubCategory = it)
                }

            }
        }
//    }
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




