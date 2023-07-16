package com.devs.adminapplication.screens.orderTracking

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
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
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.screens.addProducts.uriToFile
import com.devs.adminapplication.screens.componenents.TextBox
import com.devs.adminapplication.screens.componenents.isInteger
import com.devs.adminapplication.screens.orderHistory.DateBoxSelectable

import com.devs.adminapplication.ui.theme.PrimaryDark
import com.devs.adminapplication.ui.theme.PrimaryLight
import com.devs.adminapplication.ui.theme.PrimaryText
import com.devs.adminapplication.utils.Constants
import java.io.File
import java.time.LocalDate

@Composable
fun OrderTrackingScreen(orderTrackingViewmodel: OrderTrackingViewmodel = hiltViewModel<OrderTrackingViewmodel>()) {
    val context = LocalContext.current
    val orderID = remember { mutableStateOf("") }
    val trackingNo = remember { mutableStateOf("") }
    val companyName = remember { mutableStateOf("") }
    val departureDate = remember { mutableStateOf("") }
    val loading = orderTrackingViewmodel.loading.collectAsState()
    val failReason = orderTrackingViewmodel.failReason.collectAsState()
    var date1 by remember {
        mutableStateOf(LocalDate.now())
    }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {


        TextBox(orderID, "Order ID", focusManager, enabled = true)
        TextBox(trackingNo, "Tracking Number", focusManager, enabled = true)
        TextBox(companyName, "Company Name", focusManager, enabled = true)
        DateBoxSelectable(dateText = departureDate, label = "Departure Date") {
            date1 = it
        }
        androidx.compose.material.Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(55.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                      orderTrackingViewmodel.updateTrackingDetail(
                          TrackingState(
                              orderId = orderID.value.toLong(),
                              trackingNumber = trackingNo.value,
                              comapnyName = companyName.value,
                              departureDt = departureDate.value
                      ))
            },
            enabled = true,
            colors = androidx.compose.material.ButtonDefaults.buttonColors(
                backgroundColor = PrimaryLight
            ),
        ) {
            androidx.compose.material.Text(
                text = "ADD/UPDATE",
                color = Color.Black,
                fontSize = 17.sp
            )
        }
        if (loading.value == true)
            Column(modifier = Modifier.fillMaxWidth().padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = PrimaryDark)
            }
        if (failReason.value != " ") {
            Toast.makeText(context, failReason.value, Toast.LENGTH_SHORT).show()
            orderTrackingViewmodel.resetFailReason()
        }
//        var selected by remember {
//            mutableStateOf("Edit")
//        }
//        Spacer(modifier = Modifier.size(10.dp))
//        Row(
//            Modifier
//                .padding(horizontal = 10.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//
//            Button(
//                modifier = Modifier.width(100.dp),
//                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
//                onClick = { selected = "Edit" },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (selected == "Edit") PrimaryLight else Color.White,
//                    contentColor = PrimaryText
//                ),
//                elevation = ButtonDefaults.buttonElevation(4.dp)
//            ) {
//                Text(text = "Edit")
//            }
//            Button(
//                modifier = Modifier.width(100.dp),
//                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
//                onClick = { selected = "Add" },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (selected == "Add") PrimaryLight else Color.White,
//                    contentColor = PrimaryText
//                ),
//                elevation = ButtonDefaults.buttonElevation(4.dp)
//            ) {
//                Text(text = "Add")
//            }
//
//
//        }
//        Spacer(modifier = Modifier.size(10.dp))
////        EditBox(selected, categoryViewModel)
//        when (selected) {
//            "Edit" -> EditBox(orderTrackingViewmodel)
//            "Add" -> AddBox(orderTrackingViewmodel)
//
//        }
//        if (loading.value == true)
//            Column(modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
//                CircularProgressIndicator(color = PrimaryDark)
//            }
//        if (failReason.value != " ") {
//            Toast.makeText(context, failReason.value, Toast.LENGTH_SHORT).show()
//            orderTrackingViewmodel.resetFailReason()
//        }

    }
}


//@Composable
//fun EditBox(viewmodel: OrderTrackingViewmodel) {
//    val failReason = viewmodel.failReason.collectAsState()
//    val context = LocalContext.current
//    val orderID = remember { mutableStateOf("") }
//    val trackingNo = remember { mutableStateOf("") }
//    val companyName = remember { mutableStateOf("") }
//    val departureDate = remember { mutableStateOf("") }
//    var date1 by remember {
//        mutableStateOf(LocalDate.now())
//    }
//    val newTrackID= remember { mutableStateOf("")}
//    val focusManager = LocalFocusManager.current
//    val trackingState by viewmodel.trackingState.collectAsState()
//    TextBox(orderID, "Order ID", focusManager, enabled = true)
//    TextBox(trackingNo, "Tracking Number", focusManager, enabled = true)
//    TextBox(companyName, "Company Name", focusManager, enabled = true)
//    DateBoxSelectable(dateText = departureDate, label = "From Date") {
//        date1 = it
//    }
//    Button(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 10.dp)
//            .height(55.dp),
//        shape = RoundedCornerShape(4.dp),
//        onClick = {
//        },
//        enabled = true,
//        colors = ButtonDefaults.buttonColors(
//            containerColor = PrimaryLight
//        ),
//    ) {
//        androidx.compose.material.Text(
//            text = "Add/UPDATE",
//            color = Color.Black,
//            fontSize = 17.sp
//        )
//    }
////    if (trackingState.message=="200"){
////        val oldTrackID= remember { mutableStateOf(trackingState.trackingId) }
////        Text(text = "Old Data", fontSize = 16.sp)
////        TextBox(oldTrackID, "Tracking ID", focusManager, enabled = false)
////        Text(text = "New Data", fontSize = 16.sp)
////        TextBox(newTrackID, "Tracking ID", focusManager, enabled = true)
////        androidx.compose.material.Button(
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(top = 10.dp)
////                .height(55.dp),
////            shape = RoundedCornerShape(4.dp),
////            onClick = {
////            },
////            enabled = true,
////            colors = androidx.compose.material.ButtonDefaults.buttonColors(
////                backgroundColor = PrimaryLight
////            ),
////        ) {
////            androidx.compose.material.Text(
////                text = "UPDATE",
////                color = Color.Black,
////                fontSize = 17.sp
////            )
////        }
////    }else{
////        androidx.compose.material.Button(
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(top = 10.dp)
////                .height(55.dp),
////            shape = RoundedCornerShape(4.dp),
////            onClick = {
////            },
////            enabled = true,
////            colors = androidx.compose.material.ButtonDefaults.buttonColors(
////                backgroundColor = PrimaryLight
////            ),
////        ) {
////            androidx.compose.material.Text(
////                text = "NEXT",
////                color = Color.Black,
////                fontSize = 17.sp
////            )
////        }
////        if (failReason.value!=" "){
////            Toast.makeText(context,"Order ID not found",Toast.LENGTH_SHORT).show()
////            viewmodel.resetFailReason()
////        }
////    }
//
//
//}
//
//@Composable
//fun AddBox(viewmodel: OrderTrackingViewmodel) {
//
//}
