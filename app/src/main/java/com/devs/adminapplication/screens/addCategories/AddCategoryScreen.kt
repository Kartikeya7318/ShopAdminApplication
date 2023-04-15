package com.devs.adminapplication.screens.addCategories

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devs.adminapplication.models.util.ChipList

import com.devs.adminapplication.screens.addSubCategories.SubCategoryViewModel
import com.devs.adminapplication.screens.componenents.TextBox
import com.devs.adminapplication.screens.componenents.TextBoxSelectable
import com.devs.adminapplication.ui.theme.PrimaryLight
import com.devs.adminapplication.ui.theme.PrimaryText
import com.devs.adminapplication.utils.Constants

@Composable
fun AddCategoryScreen(categoryViewModel: AddCategoryViewModel) {
//    Text(text = "CategoryScreen")
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
                Text(text = "Edit")
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
                Text(text = "Add")
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
                Text(text = "Delete")
            }

        }
        Spacer(modifier = Modifier.size(10.dp))
//        EditBox(selected, categoryViewModel)
        when (selected) {
            "Edit" -> EditBox(categoryViewModel = categoryViewModel)
            "Add" -> AddBox(categoryViewModel = categoryViewModel)
            "Delete" -> DeleteBox(categoryViewModel = categoryViewModel)
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EditBox(categoryViewModel: AddCategoryViewModel) {
    val context = LocalContext.current
    val categoryScreenState by categoryViewModel.categoryScreenState.collectAsState()
    val catList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (categoryScreenState.categories.size)) {
        val chipList = ChipList(
            id = categoryScreenState.categories[i].id.toString(),
            name = categoryScreenState.categories[i].name.toString()
        )
//        if (categoryScreenState.categories[i].status == "Active")
            catList.add(chipList)
        Constants.CATEGORIES = catList
    }
    val expanded1 = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val categoryName = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    TextBoxSelectable(
        categoryName,
        "Category",
        focusManager,
        expanded1,
        catList,
    ) { id ->
        categoryId.value = id
    }
    if (categoryId.value != ""){
        val newCategoryName = remember { mutableStateOf(categoryName.value) }
        newCategoryName.value = categoryName.value
        Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Old Data", fontSize = 16.sp)
                TextBox(categoryName, "Category Name", focusManager, enabled = false)

            }
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "New Data", fontSize = 16.sp)
                TextBox(newCategoryName, "Category Name", focusManager, enabled = true)
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
                categoryViewModel.updateCat(newCategoryName.value,categoryId.value.toInt())
            }
        ) {
            Text(
                text = "UPDATE DATA",
                color = Color.Black,
                fontSize = 17.sp
            )
        }
    }
}

@Composable
private fun AddBox(categoryViewModel: AddCategoryViewModel){
    val focusManager = LocalFocusManager.current
    val newCategoryName = remember { mutableStateOf("") }
    var error = remember { mutableStateOf(false) }
    TextBox(newCategoryName, "Category Name", focusManager, enabled = true, isError = error)
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
            categoryViewModel.newCat(categoryName = newCategoryName.value)
        }
    ) {
        Text(
            text = "UPDATE DATA",
            color = Color.Black,
            fontSize = 17.sp
        )
    }
}

@Composable
private fun DeleteBox(categoryViewModel: AddCategoryViewModel){
    val context = LocalContext.current
    val categoryScreenState by categoryViewModel.categoryScreenState.collectAsState()
    val catList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (categoryScreenState.categories.size)) {
        val chipList = ChipList(
            id = categoryScreenState.categories[i].id.toString(),
            name = categoryScreenState.categories[i].name.toString()
        )
//        if (categoryScreenState.categories[i].status == "Active")
            catList.add(chipList)
        Constants.CATEGORIES = catList
    }
    val expanded1 = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val categoryName = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    TextBoxSelectable(
        categoryName,
        "Category",
        focusManager,
        expanded1,
        catList,
    ) { id ->
        categoryId.value = id
    }
    if (categoryId.value != ""){
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
                categoryViewModel.deleteSubCat(categoryId.value.toInt())
            }
        ) {
            Text(
                text = "Delete",
                color = Color.Black,
                fontSize = 17.sp
            )
        }
    }
}
