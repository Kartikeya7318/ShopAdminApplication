package com.devs.adminapplication.screens.navigationDrawer

import android.annotation.SuppressLint
import android.util.Log

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.ui.core.DensityAmbient
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.devs.adminapplication.R
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.screens.home.HomeViewModel
import com.devs.adminapplication.screens.home.UserAction

import com.devs.adminapplication.ui.theme.PrimaryLight
import com.devs.adminapplication.ui.theme.PrimaryText
import com.devs.adminapplication.ui.theme.SecondaryText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable

fun DrawerHeader() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(color = Color.White)
            .padding(start = 20.dp, top = 0.dp, end = 20.dp)


    ) {
        Surface(
            modifier = Modifier
                .padding(top = 20.dp)
                .size(85.dp),
            shape = CircleShape,
            elevation = 0.dp,
            border = BorderStroke(width = 1.dp, color = Color.Black)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = R.drawable.placeholder_img)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            transformations(CircleCropTransformation())
                        }).build()
                ),
                contentDescription = ""
            )

        }
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "Kartik Tripathi",
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            color = PrimaryText,
        )
        Text(
            text = "Premium Member",
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            color = SecondaryText,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Divider(thickness = 1.5.dp, color = Color.Gray)


    }
}


@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    onItemClick: (MenuItem) -> Unit
) {
    var selected by remember {
        mutableStateOf(
            MenuItem(
                id = "home",
                title = "Home",
                contentDescription = "Go to home screen",
                icon = Icons.Default.Home,
                route = AdminScreens.HomeScreen.name
            )
        )
    }
    Log.d("finders", "DrawerBody: $selected")
    DrawerHeader()
    Spacer(modifier = modifier.height(10.dp))
    LazyColumn(modifier) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .clickable {
                        onItemClick(item)
                        selected = item

                    }
                    .background(
                        color = if (selected == item) Color(0x99B6F0EC) else Color.Transparent,
                        shape = RoundedCornerShape(corner = CornerSize(10.dp))
                    )
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Icon(
                    modifier = Modifier.padding(start = 10.dp),
                    imageVector = item.icon,
                    contentDescription = item.contentDescription
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    text = item.title,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = PrimaryText
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun Menu(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    navController: NavController,
    viewModel: HomeViewModel,
    contains: @Composable () -> Unit,

    ) {
    val topBarState by viewModel.topBarState.collectAsState()
    Log.d("CheckTag1", "Menu: Products ${topBarState.allProducts?.size}")
    var selectedScreen by remember {
        mutableStateOf(AdminScreens.HomeScreen.name)
    }
    Scaffold(
        scaffoldState = scaffoldState,
//        topBar = {
//
//        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id = "home",
                        title = "Home",
                        contentDescription = "Go to home screen",
                        icon = Icons.Default.Home,
                        route = AdminScreens.HomeScreen.name
                    ),
                    MenuItem(
                        id = "add",
                        title = "Add Product",
                        contentDescription = "Go to profile screen",
                        icon = Icons.Default.Info,
                        route = AdminScreens.AddProductScreen.name
                    ),

                    MenuItem(
                        id = "category",
                        title = "Add Category",
                        contentDescription = "Statistics",
                        icon = Icons.Default.Info,
                        route = AdminScreens.AddCategoryScreen.name
                    ),
                    MenuItem(
                        id = "sub category",
                        title = "Add Sub Category",
                        contentDescription = "Get help",
                        icon = Icons.Default.Info,
                        route = AdminScreens.AddSubCategoryScreen.name
                    ),
                    MenuItem(
                        id = "brand",
                        title = "Add Brand",
                        contentDescription = "Get help",
                        icon = Icons.Default.Info,
                        route = AdminScreens.AddBrandScreen.name
                    ),
                    MenuItem(
                        id = "orderHistory",
                        title = "Order History",
                        contentDescription = "feedback",
                        icon = Icons.Default.Info,
                        route = AdminScreens.OrderHistoryScreen.name
                    ),
                    MenuItem(
                        id = "orderTracking",
                        title = "Order Tracking",
                        contentDescription = "Share",
                        icon = Icons.Default.Info,
                        route = AdminScreens.OrderTrackingScreen.name
                    ),
                    MenuItem(
                        id = "completedOrders",
                        title = "Completed Orders",
                        contentDescription = "Share",
                        icon = Icons.Default.Info,
                        route = ""
                    ),
                    MenuItem(
                        id = "signout",
                        title = "Sign Out",
                        contentDescription = "Sign Out",
                        icon = Icons.Default.Logout,
                        route = ""
                    ),
                )
            ) {
                selectedScreen = it.route
                println("Clicked on ${it.title}")
                navController.popBackStack()
                navController.navigate(it.route) {
                    launchSingleTop = true
                    restoreState = true
                }
                scope.launch {
                    scaffoldState.drawerState.close()
                }

            }
        }
    ) {
        Column() {

//            Crossfade(
//                targetState = topBarState.isSearchBarVisible,
//                animationSpec = tween(durationMillis = 200)
//            ) {
            if (topBarState.isSearchBarVisible) {
                SearchAppBar(
                    onCloseIconClicked = {
                        viewModel.onAction(UserAction.CloseIconClicked)
                    },
                    onInputValueChange = { newText ->
                        viewModel.onAction(
                            UserAction.TextFieldInput(newText)
                        )
                        Log.d("CheckTag1", "Menu: ${topBarState.searchResults?.size}")
                    },
                    text = topBarState.searchText,
                    onSearchClicked = {
//                            keyboardController?.hide()
//                            focusManager.clearFocus()
                    }
                )
            } else {
                AppBar(
                    refreshState = {
                        navController.popBackStack()
                        navController.navigate(selectedScreen) {
//                    launchSingleTop=true
//                    restoreState=true
                        }
//                        viewModel.getAllCategories()
                    },
                    onSearchClicked = { viewModel.onAction(UserAction.SearchIconClicked) },
                    selectedScreen = selectedScreen
                ) {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            }

            contains()
        }
    }
}

@Composable
fun AppBar(
    refreshState: () -> Unit,
    onSearchClicked: () -> Unit,
    selectedScreen: String,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = PrimaryLight,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.size(28.dp),
                    tint = Color.Black
                )
            }

            Text(
                text = "Shop Admin ",
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = PrimaryText,
                modifier = Modifier.weight(1f),

                )
//            if (selectedScreen==AdminScreens.HomeScreen.name) {
//                IconButton(onClick = onSearchClicked) {
//                    Icon(
//                        imageVector = Icons.Rounded.Search,
//                        contentDescription = "Done",
//                        modifier = Modifier.size(28.dp),
//                        tint = Color.Black
//                    )
//                }
//            }
            IconButton(onClick = refreshState) {
                Icon(
                    imageVector = Icons.Rounded.History,
                    contentDescription = "Done",
                    modifier = Modifier.size(28.dp),
                    tint = Color.Black
                )
            }
        }


    }
}

@ExperimentalMaterial3Api
@Composable
fun SearchAppBar(
    onCloseIconClicked: () -> Unit,
    onInputValueChange: (String) -> Unit,
    text: String,
    onSearchClicked: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 300))
        focusRequester.requestFocus()
    }

    BoxWithConstraints {
        val maxWidth = maxWidth - 40.dp // Adjust padding

        androidx.compose.material3.OutlinedTextField(
            modifier = Modifier
//                .fillMaxWidth()
                .padding(20.dp)
                .height(TextFieldDefaults.MinHeight)
                .width(maxWidth * animationProgress.value)
                .focusRequester(focusRequester),
            value = text,
            onValueChange = {
                onInputValueChange(it)
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            ),
            placeholder = {
                androidx.compose.material3.Text(
                    text = "Search...",
                    fontFamily = FontFamily.Default,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = ContentAlpha.medium)
                )
            },
            leadingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onBackground.copy(
                        alpha = ContentAlpha.medium
                    )
                )
            },
            trailingIcon = {
                androidx.compose.material3.IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onInputValueChange("")
                        } else {
                            onCloseIconClicked()
                        }
                    }
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF00BCD4),
                cursorColor = Color(0xFF00BCD4),
                focusedLabelColor = Color(0xFF00ACC1)
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchClicked() }
            ),
            // shape = RoundedCornerShape(16.dp)
        )
    }
}
