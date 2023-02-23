package com.devs.adminapplication.screens.addProducts

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devs.adminapplication.models.ProductInfo
import com.devs.adminapplication.screens.componenents.TextBox
import com.devs.adminapplication.ui.theme.BorderColor
import com.devs.adminapplication.ui.theme.PrimaryLight

@Composable
fun ProductInfoScreen(
    navController: NavController,
    addProductViewModel: AddProductViewModel = hiltViewModel()
) {
    val product:ProductInfo= ProductInfo()
    var productInfo :MutableList<ProductInfo> = emptyList<ProductInfo>().toMutableList()
    Column() {


        Column(modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .fillMaxWidth()
            .padding(
                start = 15.dp, top = 10.dp,
                end = 15.dp
            )
            .verticalScroll(rememberScrollState())) {
            for(it in 0 until addProductViewModel.product._nProducts) {
                productInfo.add(product)
                TypeBox(it, productInfo)

                Spacer(modifier = Modifier.size(10.dp))
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()

                .padding(top = 10.dp, bottom = 20.dp, start = 15.dp, end = 15.dp)
                .height(55.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {

//                Log.d("LoginFlow", "AddProductScreen: " + name.value)
//                Log.d("LoginFlow", "AddProductScreen: " + subCategoryId.value)
//                Log.d("LoginFlow", "AddProductScreen: " + brandId.value)
//                Log.d("LoginFlow", "AddProductScreen: " + quantity.value)
//                Log.d("LoginFlow", "AddProductScreen: " + price.value)
//                Log.d("LoginFlow", "AddProductScreen: " + nProducts.value)

                Log.d("LoginFlow", "ProductInfo: "+productInfo.toString())


            },
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PrimaryLight
            ),
        ) {
            androidx.compose.material.Text(
                text = "DONE",
                color = Color.Black,
                fontSize = 17.sp
            )
        }

    }
}

@Composable
fun TypeBox(i: Int, productInfo: MutableList<ProductInfo>, ) {

    val product:ProductInfo= ProductInfo()
    Column(modifier = Modifier
        .border(width = 1.dp, shape = RoundedCornerShape(5.dp), color = BorderColor)
        .padding(
            start = 15.dp, top = 10.dp,
            end = 15.dp, bottom = 10.dp
        )) {
        Text(text ="Type : "+(i+1).toString() )
        var color = remember { mutableStateOf("") }
        TextBox(name = color, label = "Color", focusManager = LocalFocusManager.current)
        var price = remember { mutableStateOf("") }
        TextBox(name = price, label = "Price", focusManager = LocalFocusManager.current)
        var size = remember { mutableStateOf("") }
        TextBox(name = size, label = "Size", focusManager = LocalFocusManager.current)
        product.color=color.value
        product.price=price.value
        product.size=size.value
        productInfo[i] = product
    }
}


