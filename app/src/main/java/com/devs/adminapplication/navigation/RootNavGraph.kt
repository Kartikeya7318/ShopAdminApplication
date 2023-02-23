package com.devs.adminapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devs.adminapplication.screens.MainActivityScreen
import com.devs.adminapplication.screens.addProducts.AddProductViewModel

@Composable
fun RootNavigationGraph(navController1: NavHostController) {
    NavHost(
        navController = navController1,
        route = Graph.ROOT.name,
        startDestination = Graph.AUTHENTICATION.name
    ) {
        authNavGraph(navController1 = navController1)
        composable(route = Graph.HOME.name) {
            MainActivityScreen(navControllerRoot=navController1)
        }



    }
}

