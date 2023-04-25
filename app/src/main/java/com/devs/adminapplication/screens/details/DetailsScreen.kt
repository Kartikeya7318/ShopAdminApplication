package com.devs.adminapplication.screens.details

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.devs.adminapplication.models.productResponse.ProductDetail
import com.devs.adminapplication.models.updateProduct.ProductUpdate
import com.devs.adminapplication.models.updateProduct.ProductUpdateInfo
import com.devs.adminapplication.models.updateProduct.ProductUpdateInfoWithKey
import com.devs.adminapplication.models.util.ChipList
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.screens.addProducts.uriToFile
import com.devs.adminapplication.screens.componenents.TextBoxSelectable
import com.devs.adminapplication.screens.componenents.TextBox
import com.devs.adminapplication.ui.theme.PrimaryLight
import com.devs.adminapplication.ui.theme.PrimaryText
import com.devs.adminapplication.utils.Constants
import com.google.gson.Gson

data class inputDialogState(
    val size: String = "",
    val color: String = "",
    val data: String = "",
    val data2: String = "",
    val label: String = "",
    val index: Int = 0
)

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun DetailsScreen(
    navController: NavHostController,
    productId: String,
    SubCategoryId: String,
    detailScreenViewModel: DetailScreenViewModel

) {
    val context = LocalContext.current
    var subCategoryId = SubCategoryId
    var brandId = 0
    val detailScreenState by detailScreenViewModel.detailScreenState.collectAsState()
    var product = detailScreenState.product
    val scaffoldState = rememberScaffoldState()
    var saveEnabled by remember {
        mutableStateOf(false)
    }
    var imageSaveEnabled by remember {
        mutableStateOf(false)
    }
//    if (product!=null){
//        detailScreenViewModel.getAllCategories()
//        detailScreenViewModel.updateSubCatList(product.subCategory.categoryId.toString())
    var openDialog by remember { mutableStateOf(false) }
    var openInputDialog = remember { mutableStateOf(false) }
    var openImageDialog = remember { mutableStateOf(false) }
    val inputDialogState = remember {
        mutableStateOf(inputDialogState())
    }
    val catList: MutableList<ChipList> = mutableListOf()

    for (i in 0 until (detailScreenState.categories.size)) {
        val chipList = ChipList(
            id = detailScreenState.categories[i].id.toString(),
            name = detailScreenState.categories[i].name
        )
        catList.add(chipList)
        Constants.CATEGORIES = catList
    }

    val subCatList: MutableList<ChipList> = mutableListOf()
    for (i in 0 until (detailScreenState.subCategories.size)) {
        val chipList = ChipList(
            id = detailScreenState.subCategories[i].id.toString(),
            name = detailScreenState.subCategories[i].name
        )
        subCatList.add(chipList)
        Log.d("reload flow", "DetailsScreen: list  " + subCatList.toString())
        Constants.SUBCATEGORIES = subCatList
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DetailBar(
                refreshState = {
                    Log.d(
                        "Refresh check",
                        AdminScreens.DetailsScreen.name + "/$productId" + "/$SubCategoryId"
                    )
                    navController.popBackStack()
                    navController.navigate(AdminScreens.DetailsScreen.name + "/$productId" + "/$SubCategoryId")
//
                },
                onBackClick = { navController.popBackStack() },
                productId.toString(),
                saveEnabled
            ) {
                if (saveEnabled) {

                    openDialog = true
                } else {
                    saveEnabled = !saveEnabled
                }
            }
        },

        ) {

        if (product != null) {
            val CategoryMap = Constants.CATEGORIES.map { it.id to it.name }.toMap()
            val SubCategoryMap = Constants.SUBCATEGORIES.map { it.id to it.name }.toMap()
            val BrandMap = Constants.BRAND.map { it.id to it.name }.toMap()

            val id = remember { mutableStateOf(product.id.toString()) }
            val name = remember { mutableStateOf(product.productName) }
            val categoryId =
                remember { mutableStateOf(CategoryMap[product.subCategory.categoryId.toString()].toString()) }

            val subCategoryName =
                remember { mutableStateOf(SubCategoryMap[product.subCategory.id.toString()].toString()) }
            Log.d("reload flow", "Details initialisation: variable " + subCategoryName.value)
            val brandName =
                remember { mutableStateOf(BrandMap[product.brand.id.toString()].toString()) }
            brandId = product.brand.id
            val priceRange = remember { mutableStateOf(product.price) }
            val focusManager = LocalFocusManager.current
            val expanded1 = remember { mutableStateOf(false) }
            val expanded2 = remember { mutableStateOf(false) }
            val expanded3 = remember { mutableStateOf(false) }
            var selectedImageUri by remember {
                mutableStateOf<Uri?>(null)
            }
            val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri -> selectedImageUri = uri }
            )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                        .height(400.dp),
                    shape = RectangleShape,

                    elevation = 4.dp
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = if (imageSaveEnabled) 0f else 0.1f),
                        modifier = Modifier.fillMaxSize(),
                        onClick = {
                            if (imageSaveEnabled)
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                        }
                    ) {}
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (imageSaveEnabled) {
                                IconButton(onClick = {
                                    imageSaveEnabled = false
                                    if (selectedImageUri!=null)
                                        openImageDialog.value = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Done,
                                        contentDescription = "Done",
                                        modifier = Modifier.size(28.dp),
                                        tint = Color.Black
                                    )
                                }
                            } else {
                                IconButton(onClick = { imageSaveEnabled = true }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Edit,
                                        contentDescription = "Edit",
                                        modifier = Modifier.size(28.dp),
                                        tint = Color.Black
                                    )
                                }
                            }
                        }
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(data = product.productImg[0].url)
                                        .apply(block = fun ImageRequest.Builder.() {
                                            crossfade(true)
                                            transformations()
                                        }).build()
                                ),
                                contentDescription = "Product Image"
                            )
                        }

                    }


                }



                TextBox(name = id, label = "ID", focusManager = focusManager, enabled = false)
                TextBox(
                    name = name,
                    label = "Name",
                    focusManager = focusManager,
                    enabled = saveEnabled
                )
                TextBoxSelectable(
                    categoryId,
                    "Category",
                    focusManager,
                    expanded1,
                    catList,
                    enabled = saveEnabled
                ) { id ->
                    detailScreenViewModel.updateSubCatList(id)
                    subCategoryName.value = ""
                }
                Log.d("reload flow", "DetailsScreen: " + subCategoryName.value)
                TextBoxSelectable(
                    subCategoryName,
                    "Sub Category",
                    focusManager,
                    expanded2,
                    subCatList,
                    enabled = saveEnabled
                ) { id ->
                    subCategoryId = id
                    Log.d("Update result", "DetailsScreen: subcategoryID= " + id)
                }
                TextBoxSelectable(
                    brandName,
                    "Brand",
                    focusManager,
                    expanded3,
                    Constants.BRAND,
                    enabled = saveEnabled
                ) { id ->
                    brandId = id.toInt()
                    Log.d("Update result", "DetailsScreen: brandID= " + id)
                }
                TextBox(
                    name = priceRange,
                    label = "Price",
                    focusManager = focusManager,
                    enabled = saveEnabled
                )
                val productDetails = product.productDetails
                Spacer(modifier = Modifier.size(10.dp))

                var productInfo by remember {
                    mutableStateOf(productDetails.toMutableList())
                }

                val toggle = remember {
                    mutableStateOf(false)
                }
                Text(
                    text = "Quantity Table",
                    color = TextFieldDefaults
                        .outlinedTextFieldColors()
                        .textColor(enabled = saveEnabled).value,
                )
                Spacer(modifier = Modifier.size(10.dp))
                TableQuantity(
                    productInfo,
                    saveEnabled,
                    inputDialogState,
                    openInputDialog,
                    toggle = toggle.value,
                    colors = product.colors,
                    sizes = product.sizes
                )
                Spacer(modifier = Modifier.size(10.dp))
//                Text(
//                    text = "Price Table",
//                    color = TextFieldDefaults
//                        .outlinedTextFieldColors()
//                        .textColor(enabled = saveEnabled).value,
//                )
//                Spacer(modifier = Modifier.size(10.dp))
//                TablePrice(
//                    productInfo,
//                    saveEnabled,
//                    inputDialogState,
//                    openInputDialog,
//                    toggle = toggle.value
//                )
                Spacer(modifier = Modifier.size(30.dp))
                InputDialogView(
                    openDialog = openInputDialog,
                    data = inputDialogState.value.data,
                    data2 = inputDialogState.value.data2,
                    label = inputDialogState.value.label,
                    size = inputDialogState.value.size,
                    color = inputDialogState.value.color
                ) { it, id ->
                    if (inputDialogState.value.index == productInfo.size)
                        productInfo.add(
                            ProductDetail(
                                id = -1,
                                status = true,
                                color = inputDialogState.value.color,
                                size = inputDialogState.value.size,
                                quantity = 0,
                                price = 0.0,
                                remaningQuantaty = 0
                            )
                        )

                    if (inputDialogState.value.label == "Price")
                        productInfo[inputDialogState.value.index].price = it.toDouble()
                    else
                        productInfo[inputDialogState.value.index].remaningQuantaty = it.toInt()
                    if (inputDialogState.value.data2 == "0.0")
                        productInfo[inputDialogState.value.index].price = id.toDouble()
                    Log.d("Update result", "DetailsScreen: " + productInfo)
                    toggle.value = !toggle.value
                    openInputDialog.value = false
                }


                if (openDialog) {

                    AlertDialog(
                        onDismissRequest = {

                            openDialog = false
                        },
                        title = {
                            Text(text = "Save Data Changes?")
                        },
                        text = {
                            Text("All the product details will be updated")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openDialog = false
                                    saveEnabled = false
                                    Log.d("Update result", "DetailsScreen: " + name.value)
//                                    Log.d("Update result", "DetailsScreen: " +subCategoryName.value)
                                    var update = ProductUpdate(
                                        name = name.value,
                                        subCategoryId = subCategoryId.toInt(),
                                        brandId = brandId,
                                        price = priceRange.value,
                                    )

                                    for (i in 0 until productInfo.size) {
                                        if (productInfo[i].id == -1) {
                                            update = update.copy(
                                                productInfo = update.productInfo +
                                                        ProductUpdateInfo(
                                                            color = productInfo[i].color,
                                                            price = productInfo[i].price,
                                                            size = productInfo[i].size,
                                                            quantity = productInfo[i].remaningQuantaty
                                                        )
                                            )
                                        } else {
                                            update = update.copy(
                                                productInfo = update.productInfo +
                                                        ProductUpdateInfoWithKey(
                                                            id = productInfo[i].id,
                                                            color = productInfo[i].color,
                                                            price = productInfo[i].price,
                                                            size = productInfo[i].size,
                                                            quantity = productInfo[i].remaningQuantaty
                                                        )
                                            )
                                        }

                                    }
                                    detailScreenViewModel.updateProductOnServer(update, product.id)
                                    Log.d(
                                        "Update result",
                                        "DetailsScreen: JSON " + Gson().toJson(update)
                                    )

                                }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    openDialog = false

                                }) {
                                Text("No")
                            }
                        }
                    )
                }

                if (openImageDialog.value) {

                    AlertDialog(
                        onDismissRequest = {

                            openImageDialog.value = false
                        },
                        title = {
                            Text(text = "Save Image Changes?")
                        },
                        text = {
                            Text("Image of the product will be updated")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openImageDialog.value = false
                                    imageSaveEnabled = false
                                    val imgFile = uriToFile(context = context, selectedImageUri!!)
                                    detailScreenViewModel.updateProductImg(prodid = id.value.toInt(), imgid =product.productImg[0].id ,imgFile);
//                                    detailScreenViewModel.updateProductOnServer(update, product.id)

                                }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    openImageDialog.value = false

                                }) {
                                Text("No")
                            }
                        }
                    )
                }


            }
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun TableQuantity(
    productInfo: MutableList<ProductDetail>,
    saveEnabled: Boolean,
    inputDialogState: MutableState<inputDialogState>,
    openInputDialog: MutableState<Boolean>,
    toggle: Boolean,
    colors:List<String>,
    sizes:List<String>
) {
//    val colors = mutableListOf<String>()
//    val sizes = listOf<String>("S", "M", "L", "XL", "XXL")
//    for (it in productInfo.indices) {
//        val color = productInfo[it].color
////        val size = productDetails[it].size
//        if (!colors.contains(color)) colors.add(color)
////        if (!sizes.contains(size)) sizes.add(size)
//    }
    Log.d("listupdate", "DetailsScreen: " + colors)
    Log.d("listupdate", "DetailsScreen: " + sizes)


    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = TextFieldDefaults
                    .outlinedTextFieldColors()
                    .placeholderColor(
                        enabled = saveEnabled
                    ).value,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(5.dp)
    ) {
        Column {
            cell("", width = 60.dp, saveEnabled = saveEnabled)
            for (color in colors) {
                cell(color, width = 60.dp, saveEnabled = saveEnabled)
            }
        }
        Row() {
            Column() {
                Row {

                    for (size in sizes) {
                        cell(size, saveEnabled = saveEnabled)
                    }
                } //header size row
                for (color in colors) {
                    Row {

                        for (size in sizes) {
                            val l = mutableStateOf(productInfo.filter { product ->
                                product.color == color && product.size == size
                            })
//                            Log.d("Update result", "DetailsScreen: " + l.value)
                            if (l.value.isNotEmpty()) {
                                val quantity = l.value[0].remaningQuantaty.toString()
                                cell(
                                    l.value[0].remaningQuantaty.toString(),
                                    saveEnabled = saveEnabled
                                ) {
                                    val index = productInfo.indexOf(l.value[0])

                                    inputDialogState.value =
                                        inputDialogState.value.copy(
                                            color = l.value[0].color,
                                            size = l.value[0].size,
                                            label = "Quantity",
                                            data = l.value[0].remaningQuantaty.toString(),
                                            data2 = l.value[0].price.toString(),
                                            index = index
                                        )
                                    openInputDialog.value = true
                                }
                            } else {
                                cell(saveEnabled = saveEnabled) {
                                    inputDialogState.value =
                                        inputDialogState.value.copy(
                                            color = color,
                                            size = size,
                                            label = "Quantity",
                                            data = "0",
                                            data2 = "0.0",
                                            index = productInfo.size
                                        )
                                    openInputDialog.value = true
                                }
                            }
                        }
                    }
                }
            }
        } //scrollable row
    }

}


@SuppressLint("UnrememberedMutableState")
@Composable
private fun TablePrice(
    productInfo: MutableList<ProductDetail>,
    saveEnabled: Boolean,
    inputDialogState: MutableState<inputDialogState>,
    openInputDialog: MutableState<Boolean>,
    toggle: Boolean
) {
    val colors = mutableListOf<String>()
    val sizes = mutableListOf<String>()

    for (it in productInfo.indices) {
        val color = productInfo[it].color
//        val size = productDetails[it].size
        if (!colors.contains(color)) colors.add(color)
//        if (!sizes.contains(size)) sizes.add(size)
    }
    Log.d("listupdate", "DetailsScreen: " + colors)
    Log.d("listupdate", "DetailsScreen: " + sizes)


    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = TextFieldDefaults
                    .outlinedTextFieldColors()
                    .placeholderColor(
                        enabled = saveEnabled
                    ).value,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(5.dp)
    ) {
        Column {
            cell("", width = 60.dp, saveEnabled = saveEnabled)
            for (color in colors) {
                cell(color, width = 60.dp, saveEnabled = saveEnabled)
            }
        }
        Row() {
            Column() {
                Row {

                    for (size in sizes) {
                        cell(size, saveEnabled = saveEnabled)
                    }
                } //header size row
                for (color in colors) {
                    Row {

                        for (size in sizes) {
                            val l = mutableStateOf(productInfo.filter { product ->
                                product.color == color && product.size == size
                            })

                            if (l.value.isNotEmpty()) {
                                val price = l.value[0].price.toString()
                                cell(
                                    l.value[0].price.toString(),
                                    saveEnabled = saveEnabled
                                ) {
                                    val index = productInfo.indexOf(l.value[0])

                                    inputDialogState.value =
                                        inputDialogState.value.copy(
                                            color = l.value[0].color,
                                            size = l.value[0].size,
                                            label = "Price",
                                            data = l.value[0].price.toString(),
                                            data2 = l.value[0].remaningQuantaty.toString(),
                                            index = index
                                        )
                                    openInputDialog.value = true


                                }
                            } else {
                                cell("0.0", saveEnabled = saveEnabled) {
                                    inputDialogState.value =
                                        inputDialogState.value.copy(
                                            color = color,
                                            size = size,
                                            label = "Price",
                                            data = "0.0",
                                            data2 = "0",
                                            index = productInfo.size
                                        )
                                    openInputDialog.value = true
                                }
                            }
                        }
                    }
                }
            }
        } //scrollable row
    }

}

@Composable
fun cell(
    text: String = "0",
    height: Dp = 30.dp,
    width: Dp = 40.dp,
    saveEnabled: Boolean,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(height)
            .border(
                width = 1.dp,
                color = TextFieldDefaults
                    .outlinedTextFieldColors()
                    .placeholderColor(
                        enabled = saveEnabled
                    ).value,
                shape = RectangleShape
            )
            .clickable {
                Log.d("Update result", "cell: " + text)
                if (saveEnabled)
                    onClick()
            },

        contentAlignment = Alignment.Center

    ) {

        Text(
            text = text,
            color = TextFieldDefaults
                .outlinedTextFieldColors()
                .textColor(enabled = saveEnabled).value,
        )

    }
}


@Composable
fun DetailBar(
    refreshState: () -> Unit,
    onBackClick: () -> Unit,
    productId: String,
    saveEnabled: Boolean,
    editSaveClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = PrimaryLight,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(28.dp),
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = "Product : $productId",
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = PrimaryText,
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = refreshState) {
                    Icon(
                        imageVector = Icons.Rounded.History,
                        contentDescription = "Done",
                        modifier = Modifier.size(28.dp),
                        tint = Color.Black
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Done",
                        modifier = Modifier.size(28.dp),
                        tint = Color.Black
                    )
                }

                if (saveEnabled) {
                    IconButton(onClick = editSaveClick) {
                        Icon(
                            imageVector = Icons.Rounded.Done,
                            contentDescription = "Done",
                            modifier = Modifier.size(28.dp),
                            tint = Color.Black
                        )
                    }
                } else {
                    IconButton(onClick = editSaveClick) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(28.dp),
                            tint = Color.Black
                        )
                    }
                }
            }


        }


    }
}

@Composable
fun InputDialogView(
    openDialog: MutableState<Boolean> = mutableStateOf(false),
    data: String,
    data2: String,
    label: String,
    size: String,
    color: String,
    onDismiss: (String, String) -> Unit
) {

    if (openDialog.value) {
        var datastate by remember {
            mutableStateOf(data)
        }
        var datastate2 by remember {
            mutableStateOf("0.0")
        }
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Input data for size : $size and color : $color")
            },
            text = {
                Column() {

                    OutlinedTextField(
                        value = datastate,
                        onValueChange = { datastate = it },
                        label = { Text(text = label) }
                    )

//                    if (label == "Quantity" && data2 == "0.0") {
//                        Spacer(modifier = Modifier.size(10.dp))
//                        OutlinedTextField(
//                            value = datastate2,
//                            onValueChange = { datastate2 = it },
//                            label = { Text(text = "Price") }
//                        )
//                    }
                }
            },
            confirmButton = {
                Button(onClick = { onDismiss(datastate, datastate2); }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

}