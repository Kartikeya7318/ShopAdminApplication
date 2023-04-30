package com.devs.adminapplication.screens.addProducts

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.ui.text.toUpperCase
import com.devs.adminapplication.models.addProduct.ProductInfo
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.screens.componenents.TextBox
import com.devs.adminapplication.screens.componenents.TextBoxSelectable
import com.devs.adminapplication.ui.theme.BorderColor
import com.devs.adminapplication.ui.theme.PrimaryDark
import com.devs.adminapplication.ui.theme.PrimaryLight
import java.util.*


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductInfoScreen(
    navController: NavController,
    addProductViewModel: AddProductViewModel = hiltViewModel()
) {
    val product: ProductInfo = ProductInfo()
    var productInfo :MutableList<ProductInfo> = emptyList<ProductInfo>().toMutableList()
    val loading = addProductViewModel.loading.collectAsState();
    val failReason = addProductViewModel.failReason.collectAsState()
    val context = LocalContext.current
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
            for(it in 0 until addProductViewModel.nProducts) {
                productInfo.add(product)
                TypeBox(it, productInfo)

                Spacer(modifier = Modifier.size(10.dp))
            }
        }
        if (loading.value == true)
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = PrimaryDark)
            }
        if (failReason.value != " ") {
            Toast.makeText(context, failReason.value, Toast.LENGTH_SHORT).show()
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
//                addProductViewModel.setProductDetails(productInfo)
                addProductViewModel.addProductToServer(productInfo){
                    Toast.makeText(context,"Product Upload Success",Toast.LENGTH_SHORT).show()

                    navController.navigate(AdminScreens.HomeScreen.name){
                        popUpTo(AdminScreens.ProductInfoScreen.name){
                            inclusive=true
                        }
                    }
                }

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

    val product = ProductInfo()
    Column(modifier = Modifier
        .border(width = 1.dp, shape = RoundedCornerShape(5.dp), color = BorderColor)
        .padding(
            start = 15.dp, top = 10.dp,
            end = 15.dp, bottom = 10.dp
        )) {
        Text(text ="Type : "+(i+1).toString() )
        var color = remember { mutableStateOf("") }
        TextBox(name = color, label = "Color", focusManager = LocalFocusManager.current)
//        var price = remember { mutableStateOf("") }
//        TextBox(name = price, label = "Price", focusManager = LocalFocusManager.current)
        var size = remember { mutableStateOf("") }
        TextBox(name = size, label = "Size", focusManager = LocalFocusManager.current)
//        val expanded1 = remember { mutableStateOf(false) }
//        TextBoxSelectableSizes(name = size, label = "Size", focusManager =LocalFocusManager.current , expanded =expanded1)
        var quantity = remember { mutableStateOf("") }
        TextBox(name = quantity, label = "Quantity", focusManager = LocalFocusManager.current)
        product.color= color.value.trim().uppercase(Locale.ROOT)
        product.size=size.value.trim().uppercase(Locale.ROOT)
        try {
            product.price = 0
            product.quantity =  quantity.value.toInt()
        }catch (e: NumberFormatException) {
            // handle the exception here
//            Log.e("MyApp", "Failed to convert '$string' to an integer", e)
            product.price=0
            product.quantity=0
        }
        productInfo[i] = product
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextBoxSelectableSizes(
    name: MutableState<String>,
    label: String,
    focusManager: FocusManager,
    expanded: MutableState<Boolean>,
    enabled: Boolean = true,
    isError: MutableState<Boolean> = mutableStateOf(false),
    onValueChange: (id: String) -> Unit = { }
) {
    val icon = if (expanded.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    var textfieldSize by remember {
        mutableStateOf(androidx.compose.ui.geometry.Size.Zero)
    }
//    val suggestions = listOf("Kotlin", "Java", "Dart", "Python")
    Column() {
        androidx.compose.material3.OutlinedTextField(
            value = name.value,
            onValueChange = { },
            label = { Text(text = label) },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
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
            ),
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded.value = !expanded.value })
            },
            enabled = enabled,
            isError = isError.value,
            supportingText = {
                if (isError.value)
                    Text(text = "*Required")
            }
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            val suggestions= listOf<String>("S","M","L","XL","XXL")
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    name.value = label
                    expanded.value = false
                    onValueChange(label)
                }) {
                    androidx.compose.material3.Text(text = label)
                }
            }
        }
    }

}


