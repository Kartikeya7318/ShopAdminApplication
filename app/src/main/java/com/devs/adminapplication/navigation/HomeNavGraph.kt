package com.devs.adminapplication.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.devs.adminapplication.screens.addBrands.AddBrandScreen
import com.devs.adminapplication.screens.addBrands.AddBrandViewModel
import com.devs.adminapplication.screens.addCategories.AddCategoryScreen
import com.devs.adminapplication.screens.addCategories.AddCategoryViewModel
import com.devs.adminapplication.screens.addProducts.AddProductScreen
import com.devs.adminapplication.screens.addProducts.AddProductViewModel

import com.devs.adminapplication.screens.addProducts.ProductInfoScreen
import com.devs.adminapplication.screens.addSubCategories.AddSubCategoryScreen
import com.devs.adminapplication.screens.addSubCategories.SubCategoryViewModel
import com.devs.adminapplication.screens.home.HomeScreen
import com.devs.adminapplication.screens.home.HomeViewModel

import com.devs.adminapplication.screens.orderHistory.OrderHistoryScreen
import com.devs.adminapplication.screens.orderHistory.OrderHistoryViewmodel
import com.devs.adminapplication.screens.orderTracking.OrderTrackingScreen
import com.devs.adminapplication.screens.orderTracking.OrderTrackingViewmodel

@Composable
fun HomeNavGraph(navController2: NavHostController, navControllerRoot: NavHostController,homeViewModel: HomeViewModel) {
    val addProductViewModel = hiltViewModel<AddProductViewModel>()
    NavHost(
        navController = navController2,
        route = Graph.HOME.name,
        startDestination = AdminScreens.HomeScreen.name
    ) {
        composable(AdminScreens.HomeScreen.name) {
            Log.d("Efficiency Check", "composabel home screen ")
            HomeScreen(navController = navControllerRoot, homeViewModel)
        }
        composable(AdminScreens.AddProductScreen.name) {
            AddProductScreen(
                navController = navController2,
                addProductViewModel = addProductViewModel,
                homeViewModel=homeViewModel
            )
        }
        composable(AdminScreens.ProductInfoScreen.name) {
            ProductInfoScreen(navController = navController2, addProductViewModel)
        }
        composable(AdminScreens.AddBrandScreen.name){
            val brandViewModel= hiltViewModel<AddBrandViewModel>()
            brandViewModel.getAllBrands()
            AddBrandScreen(brandViewModel)
        }
        composable(AdminScreens.AddCategoryScreen.name){
            val categoryViewModel= hiltViewModel<AddCategoryViewModel>()
            categoryViewModel.getAllCategories()
            AddCategoryScreen(categoryViewModel)
        }
        composable(AdminScreens.AddSubCategoryScreen.name){
            val subCategoryViewModel= hiltViewModel<SubCategoryViewModel>()
            subCategoryViewModel.getAllCategories()
            AddSubCategoryScreen(subCategoryViewModel)
        }
        composable(AdminScreens.OrderHistoryScreen.name) {
            val orderHistoryViewmodel= hiltViewModel<OrderHistoryViewmodel>()
           OrderHistoryScreen(orderHistoryViewmodel)

        }
        composable(AdminScreens.OrderTrackingScreen.name){
            val orderTrackingViewmodel= hiltViewModel<OrderTrackingViewmodel>()
            OrderTrackingScreen()
        }

    }
}
