package com.devs.adminapplication.screens.addProducts


import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.devs.adminapplication.R
import com.devs.adminapplication.models.util.ChipList
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.screens.componenents.TextBox
import com.devs.adminapplication.screens.componenents.isInteger
import com.devs.adminapplication.screens.home.HomeViewModel
import com.devs.adminapplication.ui.theme.PrimaryLight
import com.devs.adminapplication.utils.Constants
import java.io.File

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    addProductViewModel: AddProductViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel
) {
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    val context= LocalContext.current
    val name = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    var subCategoryId = remember { mutableStateOf("") }
    val brandId = remember { mutableStateOf("") }
    val quantity = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val nProducts = remember { mutableStateOf("") }
//    val imageAvailable = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val navController3 = rememberNavController()
    val homeScreenState by homeViewModel.homeScreenState.collectAsState()
    val catList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (homeScreenState.categories.size)) {
        val chipList = ChipList(
            id = homeScreenState.categories[i].id.toString(),
            name = homeScreenState.categories[i].name.toString()
        )
        catList.add(chipList)
        Constants.CATEGORIES = catList
    }
    val subCatList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (homeScreenState.subCategories.size)) {
        val chipList = ChipList(
            id = homeScreenState.subCategories[i].id.toString(),
            name = homeScreenState.subCategories[i].name.toString()
        )
        subCatList.add(chipList)

        Constants.SUBCATEGORIES = subCatList
    }
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
        Surface(
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
            if (selectedImageUri!=null){
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.placeholder_img),
                    contentDescription = "Product Image",
                    contentScale = ContentScale.FillWidth
                )
            }



        }
        val expanded1 = remember { mutableStateOf(false) }
        val expanded2 = remember { mutableStateOf(false) }
        val expanded3 = remember { mutableStateOf(false) }
        TextBox(name, "Name", focusManager)
        TextBoxSelectable(categoryId, "Category", focusManager, expanded1, catList) { id ->
            homeViewModel.updateProductListCategory(id)
            homeViewModel.getAllSubCategories()
            subCategoryId.value = ""
        }
        TextBoxSelectable(subCategoryId, "Sub Category", focusManager, expanded2, subCatList)
        TextBoxSelectable(brandId, "Brand", focusManager, expanded3, Constants.BRAND)
//        TextBox(quantity, "Quantity", focusManager)
        TextBox(price, "Price Range", focusManager)
        TextBox(nProducts, "Number of Types", focusManager)


        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(55.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                keyboardController?.hide()
                if (selectedImageUri==null) return@Button
                val imgFile=uriToFile(context = context,selectedImageUri!!)
                val CategoryMap = Constants.CATEGORIES.map { it.name to it.id }.toMap()
                val SubCategoryMap = Constants.SUBCATEGORIES.map { it.name to it.id }.toMap()
                val BrandMap = Constants.BRAND.map { it.name to it.id }.toMap()
                categoryId.value = CategoryMap[categoryId.value].toString()
                subCategoryId.value = SubCategoryMap[subCategoryId.value].toString()
                brandId.value = BrandMap[brandId.value].toString()
                Log.d("LoginFlow", "AddProductScreen: " + name.value)
                Log.d("LoginFlow", "AddProductScreen: " + categoryId.value)
                Log.d("LoginFlow", "AddProductScreen: " + subCategoryId.value)
                Log.d("LoginFlow", "AddProductScreen: " + brandId.value)
                Log.d("LoginFlow", "AddProductScreen: " + quantity.value)
                Log.d("LoginFlow", "AddProductScreen: " + price.value)
                Log.d("LoginFlow", "AddProductScreen: " + nProducts.value)
                if (isInteger(nProducts.value))
                    addProductViewModel.setProduct(
                        name = name.value.replace(" ","_"),
                        subCategoryId = subCategoryId.value,
                        brandId = brandId.value,
                        price = price.value,
                        nProducts = if (nProducts.value.isDigitsOnly()) nProducts.value.toInt() else 0 ,
                        img = imgFile
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
    enabled: Boolean = true,
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
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    name.value = label.name
                    expanded.value = false
                    onValueChange(label.id)
                }) {
                    Text(text = label.name)
                }
            }
        }
    }

}

fun uriToFile(context: Context, uri: Uri): File {
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    cursor?.moveToFirst()
    val filePathColumn = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
    val filePath = cursor?.getString(filePathColumn!!)
    cursor?.close()
    return File(filePath)
}






