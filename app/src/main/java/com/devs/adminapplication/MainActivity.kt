package com.devs.adminapplication

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.devs.adminapplication.screens.LoginScreen
import com.devs.adminapplication.ui.theme.AdminApplicationTheme
import com.devs.adminapplication.screens.Login.LoginViewModel


class MainActivity : ComponentActivity() {

    val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdminApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    val currentUser=SessionManager.getToken(getApplication().applicationContext)
//                    Log.d("LoginFlow", "onCreate: ${currentUser.toString()}")
//                    if (currentUser!=null)
//                        AdminNavigation( startDestination =AdminScreens.HomeScreen.name )
//                    else
//                        AdminNavigation( startDestination =AdminScreens.LoginScreen.name )
                    Log.d("LoginFlow", "Started")
                    LoginScreen(viewModel)
                }
            }
        }
    }


    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AdminApplicationTheme {
            Greeting("Android")
        }
    }
}
