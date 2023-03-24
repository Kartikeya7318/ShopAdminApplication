package com.devs.adminapplication.screens.navigationDrawer

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.devs.adminapplication.R
import com.devs.adminapplication.navigation.AdminScreens
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
                painter = rememberImagePainter(data = R.drawable.ic_pips,
                    builder = {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    }),
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun Menu(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    navController: NavController,
    contains: @Composable () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        },
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
                        id = "newOrders",
                        title = "New Orders",
                        contentDescription = "feedback",
                        icon = Icons.Default.Info,
                        route = ""
                    ),
                    MenuItem(
                        id = "ongoingOrders",
                        title = "Ongoing Orders",
                        contentDescription = "Share",
                        icon = Icons.Default.Info,
                        route = ""
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
                println("Clicked on ${it.title}")
                navController.popBackStack()
                navController.navigate(it.route){
                    launchSingleTop=true
                    restoreState=true
                }
                scope.launch {
                    scaffoldState.drawerState.close()
                }

            }
        }
    ) {
        contains()
    }
}

@Composable
fun AppBar(onMenuClick: () -> Unit) {
    TopAppBar(
        backgroundColor = PrimaryLight,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
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
                text = "Shop Admin Application",
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = PrimaryText,
            )
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = "Notification",
                modifier = Modifier.size(28.dp),
                tint = Color.Black
            )
        }


    }
}