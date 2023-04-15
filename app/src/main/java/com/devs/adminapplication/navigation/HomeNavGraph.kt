package com.devs.adminapplication.navigation

import androidx.compose.runtime.Composable
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
            homeViewModel.getAllCategories()
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




    }
}
