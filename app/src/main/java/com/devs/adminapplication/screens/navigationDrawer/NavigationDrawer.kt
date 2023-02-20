package com.devs.adminapplication.screens.navigationDrawer

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
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.devs.adminapplication.R
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.ui.theme.PrimaryText
import com.devs.adminapplication.ui.theme.SecondaryText

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