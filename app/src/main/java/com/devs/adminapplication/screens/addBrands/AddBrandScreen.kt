package com.devs.adminapplication.screens.addBrands

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devs.adminapplication.models.util.ChipList
import com.devs.adminapplication.screens.componenents.TextBox
import com.devs.adminapplication.screens.componenents.TextBoxSelectable

import com.devs.adminapplication.ui.theme.PrimaryLight
import com.devs.adminapplication.ui.theme.PrimaryText
import com.devs.adminapplication.utils.Constants

@Composable
fun AddBrandScreen(addBrandViewModel: AddBrandViewModel) {
//    Text(text = "BrandScreen")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var selected by remember {
            mutableStateOf("Edit")
        }
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                modifier = Modifier.width(100.dp),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                onClick = { selected = "Edit" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == "Edit") PrimaryLight else Color.White,
                    contentColor = PrimaryText
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                androidx.compose.material3.Text(text = "Edit")
            }
            Button(
                modifier = Modifier.width(100.dp),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                onClick = { selected = "Add" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == "Add") PrimaryLight else Color.White,
                    contentColor = PrimaryText
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                androidx.compose.material3.Text(text = "Add")
            }

            Button(
                modifier = Modifier.width(100.dp),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                onClick = { selected = "Delete" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == "Delete") PrimaryLight else Color.White,
                    contentColor = PrimaryText
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                androidx.compose.material3.Text(text = "Delete")
            }

        }
        Spacer(modifier = Modifier.size(10.dp))
//        EditBox(selected, categoryViewModel)
        when (selected) {
            "Edit" -> EditBox(brandViewModel = addBrandViewModel)
            "Add" -> AddBox(brandViewModel = addBrandViewModel)
            "Delete" -> DeleteBox(brandViewModel = addBrandViewModel)
        }

    }

}

@Composable
private fun EditBox(brandViewModel: AddBrandViewModel) {
    val context = LocalContext.current
    val brandScreenState by brandViewModel.addBrandState.collectAsState()
    val brandList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (brandScreenState.brands.size)) {
        val chipList = ChipList(
            id = brandScreenState.brands[i].id.toString(),
            name = brandScreenState.brands[i].name.toString()
        )
        brandList.add(chipList)
        Constants.CATEGORIES = brandList
    }
    val expanded1 = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val brandName = remember { mutableStateOf("") }
    val brandId = remember { mutableStateOf("") }
    TextBoxSelectable(
        brandName,
        "Brand",
        focusManager,
        expanded1,
        brandList,
    ) { id ->
        brandId.value = id
    }
    if (brandId.value != ""){
        val newBrandName = remember { mutableStateOf(brandName.value) }
        newBrandName.value = brandName.value
        Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.material3.Text(text = "Old Data", fontSize = 16.sp)
                TextBox(brandName, "Category Name", focusManager, enabled = false)

            }
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.material3.Text(text = "New Data", fontSize = 16.sp)
                TextBox(newBrandName, "Category Name", focusManager, enabled = true)
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(55.dp),
            shape = RoundedCornerShape(4.dp),
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryLight
            ),
            onClick = {
                brandViewModel.updateBrand(newBrandName.value,brandId.value.toInt())
            }
        ) {
            androidx.compose.material3.Text(
                text = "UPDATE DATA",
                color = Color.Black,
                fontSize = 17.sp
            )
        }
    }
}

@Composable
private fun AddBox(brandViewModel: AddBrandViewModel) {
    val focusManager = LocalFocusManager.current
    val newBrandName = remember { mutableStateOf("") }
    var error = remember { mutableStateOf(false) }
    TextBox(newBrandName, "Brand Name", focusManager, enabled = true, isError = error)
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(55.dp),
        shape = RoundedCornerShape(4.dp),
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryLight
        ),
        onClick = {
            brandViewModel.newBrand(newBrandName.value)
        }
    ) {
        androidx.compose.material3.Text(
            text = "UPDATE DATA",
            color = Color.Black,
            fontSize = 17.sp
        )
    }
}

@Composable
private fun DeleteBox(brandViewModel: AddBrandViewModel) {
    val context = LocalContext.current
    val brandScreenState by brandViewModel.addBrandState.collectAsState()
    val brandList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (brandScreenState.brands.size)) {
        val chipList = ChipList(
            id = brandScreenState.brands[i].id.toString(),
            name = brandScreenState.brands[i].name.toString()
        )
        brandList.add(chipList)
        Constants.CATEGORIES = brandList
    }
    val expanded1 = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val brandName = remember { mutableStateOf("") }
    val brandId = remember { mutableStateOf("") }
    TextBoxSelectable(
        brandName,
        "Brand",
        focusManager,
        expanded1,
        brandList,
    ) { id ->
        brandId.value = id
    }
    if (brandId.value != ""){
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(55.dp),
            shape = RoundedCornerShape(4.dp),
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryLight
            ),
            onClick = {
                brandViewModel.deleteBrand(brandId.value.toInt())
            }
        ) {
            androidx.compose.material3.Text(
                text = "DELETE",
                color = Color.Black,
                fontSize = 17.sp
            )
        }
    }
}