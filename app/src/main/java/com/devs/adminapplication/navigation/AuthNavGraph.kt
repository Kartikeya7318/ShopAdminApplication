package com.devs.adminapplication.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.devs.adminapplication.screens.LoginScreen
import com.devs.adminapplication.screens.login.LoginViewModel

fun NavGraphBuilder.authNavGraph(navController1: NavHostController){
    navigation(
        route = Graph.AUTHENTICATION.name,
        startDestination = AdminScreens.LoginScreen.name
    ) {
        composable(AdminScreens.LoginScreen.name){
            val loginViewModel= hiltViewModel<LoginViewModel>()
            LoginScreen(navController1=navController1,viewModel =loginViewModel)
        }
    }
}