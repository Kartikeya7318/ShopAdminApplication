package com.devs.adminapplication.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devs.adminapplication.screens.addProducts.AddProductScreen
import com.devs.adminapplication.screens.addProducts.AddProductViewModel

import com.devs.adminapplication.screens.addProducts.ProductInfoScreen
import com.devs.adminapplication.screens.details.DetailsScreen
import com.devs.adminapplication.screens.home.HomeScreen
import com.devs.adminapplication.screens.home.HomeViewModel

@Composable
fun HomeNavGraph(navController2: NavHostController, navControllerRoot: NavHostController) {
    val addProductViewModel = hiltViewModel<AddProductViewModel>()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    NavHost(
        navController = navController2,
        route = Graph.HOME.name,
        startDestination = AdminScreens.HomeScreen.name
    ) {
        composable(AdminScreens.HomeScreen.name) {

            HomeScreen(navController = navControllerRoot, homeViewModel)
        }
        composable(AdminScreens.AddProductScreen.name) {
            AddProductScreen(
                navController = navController2,
                addProductViewModel = addProductViewModel
            )
        }
        composable(AdminScreens.ProductInfoScreen.name) {
            ProductInfoScreen(navController = navController2, addProductViewModel)
        }



    }
}
