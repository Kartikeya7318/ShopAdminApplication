package com.devs.adminapplication.screens.addSubCategories


import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.devs.adminapplication.R
import com.devs.adminapplication.models.util.ChipList

import com.devs.adminapplication.screens.addProducts.uriToFile
import com.devs.adminapplication.screens.componenents.TextBox
import com.devs.adminapplication.screens.componenents.TextBoxSelectable
import com.devs.adminapplication.screens.home.HomeViewModel
import com.devs.adminapplication.ui.theme.PrimaryLight
import com.devs.adminapplication.ui.theme.PrimaryText
import com.devs.adminapplication.utils.Constants

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AddSubCategoryScreen(subCategoryViewModel: SubCategoryViewModel) {

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
            "Edit" -> EditBox(subCategoryViewModel = subCategoryViewModel)
            "Add" -> AddBox(subCategoryViewModel = subCategoryViewModel)
            "Delete" -> DeleteBox(subCategoryViewModel = subCategoryViewModel)
        }

    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EditBox(subCategoryViewModel: SubCategoryViewModel) {
    val context = LocalContext.current
    val subCategoryScreenState by subCategoryViewModel.subCategoryScreenState.collectAsState()
    val catList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (subCategoryScreenState.categories.size)) {
        val chipList = ChipList(
            id = subCategoryScreenState.categories[i].id.toString(),
            name = subCategoryScreenState.categories[i].name.toString()
        )
        if (subCategoryScreenState.categories[i].status == "Active")
            catList.add(chipList)
        Constants.CATEGORIES = catList
    }
    val subCatList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (subCategoryScreenState.subCategories.size)) {
        val chipList = ChipList(
            id = subCategoryScreenState.subCategories[i].id.toString(),
            name = subCategoryScreenState.subCategories[i].name.toString()
        )
        subCatList.add(chipList)

        Constants.SUBCATEGORIES = subCatList
    }
    val expanded1 = remember { mutableStateOf(false) }
    val expanded2 = remember { mutableStateOf(false) }
    var error2 = remember { mutableStateOf(false) }
    var error3 = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val categoryName = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    var subCategoryName = remember { mutableStateOf("") }
    var subCategoryId = remember { mutableStateOf("") }
    TextBoxSelectable(
        categoryName,
        "Category",
        focusManager,
        expanded1,
        catList,
        isError = error2
    ) { id ->
        categoryId.value = id
        subCategoryViewModel.updateSubCatList(id)
        subCategoryId.value = ""
        subCategoryName.value = ""
    }
    TextBoxSelectable(
        subCategoryName,
        "Sub Category",
        focusManager,
        expanded2,
        subCatList,
        isError = error3
    ) { id ->
        subCategoryId.value = id
    }
    if (subCategoryId.value != "" && categoryId.value != "") {
        val newcategoryId = remember { mutableStateOf(categoryId.value) }
        var newsubCategoryName = remember { mutableStateOf(subCategoryName.value) }
        val newCategoryName = remember { mutableStateOf(categoryName.value) }
        newCategoryName.value = categoryName.value
        newsubCategoryName.value = subCategoryName.value
        newcategoryId.value = categoryId.value
        Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Old Data", fontSize = 16.sp)
                TextBox(categoryName, "Category Name", focusManager, enabled = false)
                TextBox(subCategoryName, "Sub Category Name", focusManager, enabled = false)
            }
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "New Data", fontSize = 16.sp)
                val expanded3 = remember { mutableStateOf(false) }
                TextBoxSelectable(
                    newCategoryName,
                    "Category",
                    focusManager,
                    expanded3,
                    catList,
                    isError = error2
                ) { id ->
                    newcategoryId.value = id
                }
                TextBox(newsubCategoryName, "Sub Category Name", focusManager, enabled = true)
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
                Toast.makeText(
                    context,
                    "category id-> ${newcategoryId.value} \n subCategory name-> ${newsubCategoryName.value} \n SubCategoryId-> ${subCategoryId.value}",
                    Toast.LENGTH_LONG
                ).show()
                Log.d(
                    "Subcatcheck",
                    "category id-> ${newcategoryId.value} \\n subCategory name-> ${newsubCategoryName.value} \\n SubCategoryId-> ${subCategoryId.value}"
                )
                subCategoryViewModel.updateSubCat(
                    NewSubCategory(
                        categoryId= newcategoryId.value.toInt(),
                        subCategoryName = newsubCategoryName.value
                    ),
                    subCategoryId.value.toInt()
                )
            }
        ) {
            Text(
                text = "UPDATE DATA",
                color = Color.Black,
                fontSize = 17.sp
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        var selectedImageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> selectedImageUri = uri }
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Old Image", fontSize = 16.sp)
                androidx.compose.material.Surface(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RectangleShape,
                    elevation = 4.dp,

                    ) {

                    val imgurl = subCategoryScreenState.subCategories.filter { subCategory ->
                        subCategory.id == subCategoryId.value.toInt()
                    }[0].url
                    Log.d("Subcatcheck", imgurl)
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = imgurl)
                                .apply(block = fun ImageRequest.Builder.() {
                                    crossfade(true)
                                    transformations()
                                }).build()
                        ),
                        contentDescription = "Product Image"
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "New Image", fontSize = 16.sp)
                androidx.compose.material.Surface(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RectangleShape,
                    elevation = 4.dp,
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.placeholder_img),
                            contentDescription = "Product Image",
                            contentScale = ContentScale.FillWidth
                        )
                    }

                }
            }

        }
        Spacer(modifier = Modifier.size(20.dp))
        if (selectedImageUri != null) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .height(55.dp),
                shape = RoundedCornerShape(4.dp),
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryLight
                ),
                onClick = {
//                    Toast.makeText(
//                        context,
//                        "category id-> ${newcategoryId.value} \n subCategory name-> ${newsubCategoryName.value} \n SubCategoryId-> ${subCategoryId.value}",
//                        Toast.LENGTH_LONG
//                    ).show()
//                    Log.d(
//                        "Subcatcheck",
//                        "category id-> ${newcategoryId.value} \\n subCategory name-> ${newsubCategoryName.value} \\n SubCategoryId-> ${subCategoryId.value}"
//                    )
                    if (selectedImageUri == null) {
                        Toast.makeText(context, "Image Required", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val imgFile = uriToFile(context = context, selectedImageUri!!)
                    subCategoryViewModel.updateSubCatImg(id = subCategoryId.value.toInt(), img = imgFile)

                }
            ) {
                Text(
                    text = "UPDATE IMAGE",
                    color = Color.Black,
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.size(10.dp))
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AddBox(subCategoryViewModel: SubCategoryViewModel) {
    val subCategoryScreenState by subCategoryViewModel.subCategoryScreenState.collectAsState()
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val categoryName = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    val expanded1 = remember { mutableStateOf(false) }
    val catList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (subCategoryScreenState.categories.size)) {
        val chipList = ChipList(
            id = subCategoryScreenState.categories[i].id.toString(),
            name = subCategoryScreenState.categories[i].name.toString()
        )
//        if (subCategoryScreenState.categories[i].status == "Active")
            catList.add(chipList)
        Constants.CATEGORIES = catList
    }
    androidx.compose.material.Surface(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(),
        shape = RectangleShape,
        elevation = 4.dp,
        onClick = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    ) {
        if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.placeholder_img),
                contentDescription = "Product Image",
                contentScale = ContentScale.FillWidth
            )
        }

    }
    TextBoxSelectable(
        categoryName,
        "Category",
        focusManager,
        expanded1,
        catList,
    ) { id ->
        categoryId.value = id
    }
    if (categoryId.value != "") {
        var newsubCategoryName = remember { mutableStateOf("") }
        TextBox(newsubCategoryName, "Sub Category Name", focusManager, enabled = true)

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
                Log.d(
                    "Subcatcheck",
                    "category id-> ${categoryId.value} \\n subCategory name-> ${newsubCategoryName.value} "
                )
                if (selectedImageUri == null) {
                    Toast.makeText(context, "Image Required", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val imgFile = uriToFile(context = context, selectedImageUri!!)
                    subCategoryViewModel.newSubCat(
                        NewSubCategory(
                            categoryId=categoryId.value.toInt(),
                            subCategoryName = newsubCategoryName.value
                    ),
                      imgFile
                    )

            }
        ) {
            Text(
                text = "DONE",
                color = Color.Black,
                fontSize = 17.sp
            )
        }
    }
}

@Composable
private fun DeleteBox(subCategoryViewModel: SubCategoryViewModel) {
    val context = LocalContext.current
    val subCategoryScreenState by subCategoryViewModel.subCategoryScreenState.collectAsState()
    val catList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (subCategoryScreenState.categories.size)) {
        val chipList = ChipList(
            id = subCategoryScreenState.categories[i].id.toString(),
            name = subCategoryScreenState.categories[i].name.toString()
        )
//        if (subCategoryScreenState.categories[i].status == "Active")
            catList.add(chipList)
        Constants.CATEGORIES = catList
    }
    val subCatList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (subCategoryScreenState.subCategories.size)) {
        val chipList = ChipList(
            id = subCategoryScreenState.subCategories[i].id.toString(),
            name = subCategoryScreenState.subCategories[i].name.toString()
        )
        subCatList.add(chipList)

        Constants.SUBCATEGORIES = subCatList
    }
    val expanded1 = remember { mutableStateOf(false) }
    val expanded2 = remember { mutableStateOf(false) }
    var error2 = remember { mutableStateOf(false) }
    var error3 = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val categoryName = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    var subCategoryName = remember { mutableStateOf("") }
    var subCategoryId = remember { mutableStateOf("") }
    TextBoxSelectable(
        categoryName,
        "Category",
        focusManager,
        expanded1,
        catList,
        isError = error2
    ) { id ->
        categoryId.value = id
        subCategoryViewModel.updateSubCatList(id)
        subCategoryId.value = ""
        subCategoryName.value = ""
    }
    TextBoxSelectable(
        subCategoryName,
        "Sub Category",
        focusManager,
        expanded2,
        subCatList,
        isError = error3
    ) { id ->
        subCategoryId.value = id
    }
    if (subCategoryId.value != "" && categoryId.value != "") {
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
                subCategoryViewModel.deleteSubCat(subCategoryId.value.toInt())
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


