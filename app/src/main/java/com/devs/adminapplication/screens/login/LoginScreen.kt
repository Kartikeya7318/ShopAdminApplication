package com.devs.adminapplication.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devs.adminapplication.R
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.navigation.Graph
import com.devs.adminapplication.ui.theme.PrimaryDark
import com.devs.adminapplication.screens.login.LoginViewModel
import com.devs.adminapplication.utils.MyPreference


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navController1: NavController,
    viewModel: LoginViewModel,

    ) {
    val preference= MyPreference(context = LocalContext.current)
    //ui
    var email by remember {
        mutableStateOf(preference.getUserId())
    }
    var password by remember {
        mutableStateOf(preference.getPass())
    }
    var passwordVisibility by remember {
        mutableStateOf(true)
    }
//
    val loading = viewModel.loading.collectAsState();
    val failReason = viewModel.failReason.collectAsState()


    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.ic_baseline_visibility_24)
    else
        painterResource(id = R.drawable.ic_baseline_visibility_off_24)



    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(
                start = 30.dp,
                top = 180.dp,
                end = 30.dp
            )
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Login",
            modifier = Modifier.padding(0.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
        Text(
            text = "Please sign in to continue.",
            color = Color.Gray,
            fontSize = 18.sp
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                // TODO you're action goes here
                focusManager.moveFocus(FocusDirection.Down)
            }),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF00BCD4),
                cursorColor = Color(0xFF00BCD4),
                focusedLabelColor = Color(0xFF00ACC1)
            )
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(painter = icon, contentDescription = null)
                }
            },
            visualTransformation = if (passwordVisibility)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                // TODO you're action goes here
                focusManager.clearFocus()
                keyboardController?.hide()
            }),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF00BCD4),
                cursorColor = Color(0xFF00BCD4),
                focusedLabelColor = Color(0xFF00ACC1)
            )
        )
        val context = LocalContext.current
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(55.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                keyboardController?.hide()
                viewModel.loginUser(email, password){
//                    Log.d("LoginFlow", "transition start")
                    navController1.popBackStack()
                    navController1.navigate(Graph.HOME.name)
                }

            },
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PrimaryDark
            ),
        ) {
            Text(
                text = "LOGIN",
                color = Color.Black,
                fontSize = 17.sp
            )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            Spacer(modifier = Modifier.height(20.dp))
            if (loading.value == true)
                CircularProgressIndicator(color = PrimaryDark)
            if (failReason.value != " ") {
                Toast.makeText(context, viewModel.failReason.value, Toast.LENGTH_SHORT).show()
                viewModel.resetFailReason()
            }

        }
    }
}
