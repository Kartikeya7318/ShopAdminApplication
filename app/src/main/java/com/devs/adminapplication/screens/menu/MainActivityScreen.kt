package com.devs.adminapplication.screens

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.devs.adminapplication.navigation.HomeNavGraph
import com.devs.adminapplication.screens.home.HomeViewModel

import com.devs.adminapplication.screens.navigationDrawer.Menu

@Composable
fun MainActivityScreen(
    navController2: NavHostController = rememberNavController(),
    navControllerRoot: NavHostController,
    viewModel: HomeViewModel
){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Menu(scaffoldState = scaffoldState, scope =scope , navController =navController2, viewModel = viewModel ) {
        HomeNavGraph(navControllerRoot=navControllerRoot ,navController2 = navController2, homeViewModel = viewModel)
    }

}