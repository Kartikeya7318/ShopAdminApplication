package com.devs.adminapplication.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devs.adminapplication.screens.addProducts.AddProductScreen
import com.devs.adminapplication.screens.addProducts.AddProductViewModel

import com.devs.adminapplication.screens.addProducts.ProductInfoScreen
import com.devs.adminapplication.screens.home.HomeScreen
import com.devs.adminapplication.screens.home.HomeViewModel

@Composable
fun HomeNavGraph(navController2: NavHostController, navControllerRoot: NavHostController) {
    val addProductViewModel= hiltViewModel<AddProductViewModel>()
    NavHost(
        navController = navController2,
        route = Graph.HOME.name,
        startDestination = AdminScreens.HomeScreen.name
    ){
        composable(AdminScreens.HomeScreen.name){
            val homeViewModel= hiltViewModel<HomeViewModel>()
            HomeScreen(navController=navController2,homeViewModel)
        }
        composable(AdminScreens.AddProductScreen.name){
            AddProductScreen(navController = navController2, addProductViewModel = addProductViewModel)
        }
        composable(AdminScreens.ProductInfoScreen.name){
            ProductInfoScreen(navController = navController2,addProductViewModel)
        }

    }
}
