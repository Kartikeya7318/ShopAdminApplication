package com.devs.adminapplication.screens.details

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.devs.adminapplication.models.addProduct.ProductInfo
import com.devs.adminapplication.models.productResponse.Product
import com.devs.adminapplication.models.productResponse.ProductDetail
import com.devs.adminapplication.screens.addProducts.TextBoxSelectable
import com.devs.adminapplication.screens.addProducts.TypeBox
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
    productId: String?,
    homeViewModel: HomeViewModel
) {
//    Text(text = productId.toString())
    val homeScreenState by homeViewModel.homeScreenState.collectAsState()
    val product = homeScreenState.products?.filter { product ->
        product.id == productId?.toInt()
    }
//    Text(text = product.toString())
    val scaffoldState = rememberScaffoldState()
    var saveEnabled by remember {
        mutableStateOf(false)
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DetailBar(
                onBackClick = { navController.popBackStack() },
                productId.toString(),
                saveEnabled
            ) {
                saveEnabled = !saveEnabled
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
                        painter = rememberImagePainter(data = product.get(0).productImg.get(0).url,
                            builder = {
                                crossfade(true)
                                transformations()
                            }),
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
                    Constants.CATEGORIES.subList(1, Constants.CATEGORIES.size),
                    enabled = saveEnabled
                )
                TextBoxSelectable(
                    subCategoryId,
                    "Sub Category",
                    focusManager,
                    expanded2,
                    Constants.SUBCATEGORIES.subList(1, Constants.SUBCATEGORIES.size),
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
                val productDetails=product[0].productDetails
                Spacer(modifier = Modifier.size(10.dp))
                var productInfo :MutableList<ProductDetail> = productDetails.toMutableList()
                for(it in productDetails.indices) {

                    DetailBox(it,productInfo,saveEnabled)
                    Spacer(modifier = Modifier.size(10.dp))
                }
            }
        }

    }
}

@Composable
fun DetailBox(it: Int, productInfo: MutableList<ProductDetail>,enabled : Boolean) {
    Column(modifier = Modifier
        .border(width = 1.dp, shape = RoundedCornerShape(5.dp), color = BorderColor)
        .padding(
            start = 15.dp, top = 10.dp,
            end = 15.dp, bottom = 10.dp
        )) {
        var id = remember { mutableStateOf(productInfo[it].id.toString()) }
        TextBox(name = id, label = "Id", focusManager = LocalFocusManager.current,enabled)
        var color = remember { mutableStateOf(productInfo[it].color) }
        TextBox(name = color, label = "Color", focusManager = LocalFocusManager.current,enabled)
        var price = remember { mutableStateOf(productInfo[it].price.toString()) }
        TextBox(name = price, label = "Price", focusManager = LocalFocusManager.current,enabled)
        var size = remember { mutableStateOf(productInfo[it].size) }
        TextBox(name = size, label = "Size", focusManager = LocalFocusManager.current,enabled)
        var quantity = remember { mutableStateOf(productInfo[it].quantity.toString()) }
        TextBox(name = quantity, label = "Quantity", focusManager = LocalFocusManager.current,enabled)
        var remaining = remember { mutableStateOf(productInfo[it].remaningQuantaty.toString()) }
        TextBox(name = remaining, label = "Remaining Quantity", focusManager = LocalFocusManager.current,enabled)
        productInfo[it].color=color.value
        productInfo[it].price=price.value.toDouble()
        productInfo[it].size=size.value
        productInfo[it].id=id.value.toInt()
        productInfo[it].quantity=quantity.value.toInt()
        productInfo[it].remaningQuantaty=quantity.value.toInt()

    }
}


@Composable
fun DetailBar(
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

                if (saveEnabled) {
                    IconButton(onClick = editSaveClick, enabled = saveEnabled) {
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