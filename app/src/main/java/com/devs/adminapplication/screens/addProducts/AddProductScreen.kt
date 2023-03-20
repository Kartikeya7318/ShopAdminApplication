package com.devs.adminapplication.screens.addProducts


import android.annotation.SuppressLint
import android.provider.ContactsContract.Contacts.AggregationSuggestions
import android.util.Log
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
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devs.adminapplication.models.util.ChipList
import com.devs.adminapplication.navigation.AdminScreens

import com.devs.adminapplication.screens.componenents.TextBox
import com.devs.adminapplication.screens.componenents.isInteger
import com.devs.adminapplication.ui.theme.PrimaryLight
import com.devs.adminapplication.utils.Constants

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    addProductViewModel: AddProductViewModel = hiltViewModel()
) {
    val name = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    val subCategoryId = remember { mutableStateOf("") }
    val brandId = remember { mutableStateOf("") }
    val quantity = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val nProducts = remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val navController3 = rememberNavController()


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(
                start = 30.dp, top = 10.dp,
                end = 30.dp
            )
            .verticalScroll(rememberScrollState())

    ) {

        val expanded1 = remember { mutableStateOf(false) }
        val expanded2 = remember { mutableStateOf(false) }
        val expanded3 = remember { mutableStateOf(false) }
        TextBox(name, "Name", focusManager)
        TextBoxSelectable(categoryId, "Category", focusManager, expanded1,Constants.CATEGORIES.subList(1,Constants.CATEGORIES.size))
        TextBoxSelectable(subCategoryId, "Sub Category", focusManager, expanded2, Constants.SUBCATEGORIES.subList(1,Constants.SUBCATEGORIES.size))
        TextBoxSelectable(brandId, "Brand", focusManager, expanded3, Constants.BRAND)
        TextBox(quantity, "Quantity", focusManager)
        TextBox(price, "Price", focusManager)
        TextBox(nProducts, "Number of Types", focusManager)


        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(55.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                keyboardController?.hide()
                val CategoryMap = Constants.CATEGORIES.map { it.name to it.id }.toMap()
                val SubCategoryMap = Constants.SUBCATEGORIES.map { it.name to it.id }.toMap()
                val BrandMap = Constants.BRAND.map { it.name to it.id }.toMap()
                categoryId.value= CategoryMap[categoryId.value].toString()
                subCategoryId.value=SubCategoryMap[subCategoryId.value].toString()
                brandId.value=BrandMap[brandId.value].toString()
                Log.d("LoginFlow", "AddProductScreen: " + name.value)
                Log.d("LoginFlow", "AddProductScreen: " + categoryId.value)
                Log.d("LoginFlow", "AddProductScreen: " + subCategoryId.value)
                Log.d("LoginFlow", "AddProductScreen: " + brandId.value)
                Log.d("LoginFlow", "AddProductScreen: " + quantity.value)
                Log.d("LoginFlow", "AddProductScreen: " + price.value)
                Log.d("LoginFlow", "AddProductScreen: " + nProducts.value)
                if (isInteger(nProducts.value))
                    addProductViewModel.setProduct(
                        name = name.value,
                        subCategoryId = subCategoryId.value,
                        brandId = brandId.value,
                        quantity = quantity.value,
                        price = price.value,
                        nProducts = nProducts.value.toInt()
                    )
                navController.navigate(AdminScreens.ProductInfoScreen.name)

            },
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PrimaryLight
            ),
        ) {
            Text(
                text = "NEXT",
                color = Color.Black,
                fontSize = 17.sp
            )
        }
//        val n:Int=if(isInteger( nProducts.value)) nProducts.value.toInt() else 4
//        for (i in 1..n){
//            var text = remember { mutableStateOf("") }
//
//            TextBox(name = text, label ="Type" , focusManager =focusManager )
//        }


    }


}

@Composable
fun TextBoxSelectable(
    name: MutableState<String>,
    label: String,
    focusManager: FocusManager,
    expanded: MutableState<Boolean>,
    suggestions: MutableList<ChipList>,
    enabled : Boolean=true
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
        OutlinedTextField(
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
            enabled = enabled
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textfieldSize.width.toDp()})
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    name.value = label.name
                    expanded.value = false
                }) {
                    Text(text = label.name)
                }
            }
        }
    }

}





