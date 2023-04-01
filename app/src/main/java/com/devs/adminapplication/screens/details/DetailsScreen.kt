package com.devs.adminapplication.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.devs.adminapplication.models.productResponse.ProductDetail
import com.devs.adminapplication.models.util.ChipList
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.screens.addProducts.TextBoxSelectable
import com.devs.adminapplication.screens.componenents.TextBox
import com.devs.adminapplication.screens.home.HomeViewModel
import com.devs.adminapplication.ui.theme.BorderColor
import com.devs.adminapplication.ui.theme.PrimaryLight
import com.devs.adminapplication.ui.theme.PrimaryText
import com.devs.adminapplication.utils.Constants

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailsScreen(
    navController: NavHostController,
    productId: String,
    subCategoryId: String,
    homeViewModel: HomeViewModel
) {
//    Text(text = productId.toString())
    val homeScreenState by homeViewModel.homeScreenState.collectAsState()
    homeViewModel.getProducts(productListSubCategory = subCategoryId)
    val product = homeScreenState.products?.filter { product ->
        product.id == productId?.toInt()

    }
//    Text(text = product.toString())
    val scaffoldState = rememberScaffoldState()
    var saveEnabled by remember {
        mutableStateOf(false)
    }
    var openDialog by remember { mutableStateOf(false) }
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
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DetailBar(
                refreshState = {
                    navController.popBackStack()
                    navController.navigate(AdminScreens.DetailsScreen.name + "/$productId"+"/$subCategoryId")
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
//        Text(text = product.toString())
        if (product != null && product.isNotEmpty()) {
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

                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = product.get(0).productImg.get(0).url).apply(block = fun ImageRequest.Builder.() {
                                    crossfade(true)
                                    transformations()
                                }).build()
                        ),
                        contentDescription = "Product Image"
                    )


                }

                val CategoryMap = Constants.CATEGORIES.map { it.id to it.name }.toMap()
                val SubCategoryMap = Constants.SUBCATEGORIES.map { it.id to it.name }.toMap()
                val BrandMap = Constants.BRAND.map { it.id to it.name }.toMap()

                val id = remember { mutableStateOf(product[0].id.toString()) }
                val name = remember { mutableStateOf(product[0].productName) }
                val categoryId =
                    remember { mutableStateOf(CategoryMap[product[0].subCategory.categoryId.toString()].toString()) }
                val subCategoryId =
                    remember { mutableStateOf(SubCategoryMap[product[0].subCategory.id.toString()].toString()) }
                val brandId =
                    remember { mutableStateOf(BrandMap[product[0].brand.id.toString()].toString()) }
                val focusManager = LocalFocusManager.current
                val expanded1 = remember { mutableStateOf(false) }
                val expanded2 = remember { mutableStateOf(false) }
                val expanded3 = remember { mutableStateOf(false) }

                TextBox(name = id, label = "ID", focusManager = focusManager, enabled = saveEnabled)
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
                    homeViewModel.updateSubCatList(id)
                    subCategoryId.value = ""
                }
                TextBoxSelectable(
                    subCategoryId,
                    "Sub Category",
                    focusManager,
                    expanded2,
                    subCatList,
                    enabled = saveEnabled
                )
                TextBoxSelectable(
                    brandId,
                    "Brand",
                    focusManager,
                    expanded3,
                    Constants.BRAND,
                    enabled = saveEnabled
                )
                val productDetails = product[0].productDetails
                Spacer(modifier = Modifier.size(10.dp))
                var productInfo: MutableList<ProductDetail> = productDetails.toMutableList()
//                for(it in productDetails.indices) {
//                    DetailBox(it,productInfo,saveEnabled)
//                    Spacer(modifier = Modifier.size(10.dp))
//                }
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
                Table(productDetails, saveEnabled)
                Spacer(modifier = Modifier.size(30.dp))
//                cell(text = "25")
            }
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun Table(productDetails: List<ProductDetail>, saveEnabled: Boolean) {
    val colors = mutableListOf<String>()
    val sizes = listOf<String>("S", "M", "L", "XL", "XXL")
    for (it in productDetails.indices) {
        val color = productDetails[it].color
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
                            val l = productDetails.filter { product ->
                                product.color == color && product.size == size
                            }
                            if (l.isNotEmpty()) {
                                val quantity by remember {
                                    mutableStateOf(l[0].remaningQuantaty.toString())
                                }
                                cell(
                                    quantity,
                                    saveEnabled = saveEnabled
                                )
                            } else {
                                cell(saveEnabled = saveEnabled)
                            }
                        }
                    }
                }
            }
        } //scrollable row
    }

}

@Composable
fun cell(text: String = "0", height: Dp = 30.dp, width: Dp = 40.dp, saveEnabled: Boolean) {
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
            ),

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
fun DetailBox(it: Int, productInfo: MutableList<ProductDetail>, enabled: Boolean) {
    Column(
        modifier = Modifier
            .border(width = 1.dp, shape = RoundedCornerShape(5.dp), color = BorderColor)
            .padding(
                start = 15.dp, top = 10.dp,
                end = 15.dp, bottom = 10.dp
            )
    ) {
        var id = remember { mutableStateOf(productInfo[it].id.toString()) }
        TextBox(name = id, label = "Id", focusManager = LocalFocusManager.current, enabled)
        var color = remember { mutableStateOf(productInfo[it].color) }
        TextBox(name = color, label = "Color", focusManager = LocalFocusManager.current, enabled)
        var price = remember { mutableStateOf(productInfo[it].price.toString()) }
        TextBox(name = price, label = "Price", focusManager = LocalFocusManager.current, enabled)
        var size = remember { mutableStateOf(productInfo[it].size) }
        TextBox(name = size, label = "Size", focusManager = LocalFocusManager.current, enabled)
        var quantity = remember { mutableStateOf(productInfo[it].quantity.toString()) }
        TextBox(
            name = quantity,
            label = "Quantity",
            focusManager = LocalFocusManager.current,
            enabled
        )
        var remaining = remember { mutableStateOf(productInfo[it].remaningQuantaty.toString()) }
        TextBox(
            name = remaining,
            label = "Remaining Quantity",
            focusManager = LocalFocusManager.current,
            enabled
        )
        productInfo[it].color = color.value
        productInfo[it].price = price.value.toDouble()
        productInfo[it].size = size.value
        productInfo[it].id = id.value.toInt()
        productInfo[it].quantity = quantity.value.toInt()
        productInfo[it].remaningQuantaty = quantity.value.toInt()

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