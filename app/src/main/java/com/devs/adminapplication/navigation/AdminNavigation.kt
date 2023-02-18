package com.devs.adminapplication.navigation

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devs.adminapplication.screens.login.LoginViewModel
import com.devs.adminapplication.screens.LoginScreen
import com.devs.adminapplication.screens.home.HomeScreen

@Composable
fun AdminNavigation(startDestination: String) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(AdminScreens.LoginScreen.name){
            val loginViewModel= hiltViewModel<LoginViewModel>()
            LoginScreen(navController=navController,viewModel =loginViewModel)
        }
        composable(AdminScreens.HomeScreen.name){
            HomeScreen()
        }
    }
}