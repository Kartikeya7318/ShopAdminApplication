package com.devs.adminapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devs.adminapplication.screens.MainActivityScreen
import com.devs.adminapplication.screens.addProducts.AddProductViewModel
import com.devs.adminapplication.screens.details.DetailsScreen
import com.devs.adminapplication.screens.home.HomeViewModel

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
        composable(AdminScreens.DetailsScreen.name + "/{id}"+"/{subId}",
            arguments = listOf(navArgument(name = "id") { type = NavType.StringType },
                navArgument(name = "subId"){type= NavType.StringType}
            )
        ) { backStackEntry ->
            val homeViewModel = hiltViewModel<HomeViewModel>()
            DetailsScreen(navController = navController1,
                backStackEntry.arguments?.getString("id")!!,backStackEntry.arguments?.getString("subId")!!,homeViewModel)
        }



    }
}

