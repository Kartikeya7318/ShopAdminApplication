package com.devs.adminapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.preference.PreferenceManager
import com.devs.adminapplication.navigation.AdminNavigation
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.utils.Constants
import com.devs.adminapplication.ui.theme.AdminApplicationTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    var sharedpreferences = application?.getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
//    val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        var prefs=PreferenceManager.getDefaultSharedPreferences(this)
        var token:String?=null
        super.onCreate(savedInstanceState)
        setContent {
            AdminApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    val currentUser=SessionManager.getToken(application.applicationContext)
//                    Log.d("LoginFlow", "onCreate: ${currentUser.toString()}")
//                    if (currentUser!=null)
//                        AdminNavigation( startDestination =AdminScreens.HomeScreen.name,sharedpreferences=sharedpreferences )
//                    else

                    Log.d("LoginFlow", "onCreate: "+ prefs.getString(Constants.USER_TOKEN,"ndjci"))
                    token=prefs.getString(Constants.USER_TOKEN,null)
                    Log.d("LoginFlow", "onCreate: "+ token)
                    if (token!=null)
                        token=null
                    Log.d("LoginFlow", "onCreate: "+ token)
                        AdminNavigation(
                            startDestination =AdminScreens.LoginScreen.name
                        )


                    Log.d("LoginFlow", "Started")
//                    LoginScreen(viewModel)
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
