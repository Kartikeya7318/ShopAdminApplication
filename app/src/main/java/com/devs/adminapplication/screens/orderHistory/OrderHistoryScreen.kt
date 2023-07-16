package com.devs.adminapplication.screens.orderHistory

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext

//import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.content.FileProvider.getUriForFile
import com.devs.adminapplication.screens.componenents.TextBox
import com.devs.adminapplication.screens.details.cell
import com.devs.adminapplication.ui.theme.PrimaryDark
import com.devs.adminapplication.ui.theme.PrimaryLight
import com.devs.adminapplication.ui.theme.PrimaryText
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(orderHistoryViewmodel: OrderHistoryViewmodel) {

    val orderHistoryState by orderHistoryViewmodel.orderHistoryState.collectAsState()
    var fileSaved = remember {
        mutableStateOf(false)
    }
    val loading by orderHistoryViewmodel.loading.collectAsState();
    val context = LocalContext.current

    var outputmessage by remember {
        mutableStateOf("")
    }

    val openFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            // Handle success
        }
    }
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss")
    val formatted = currentDateTime.format(formatter)
    val fileName = "Order_History_$formatted.xlsx"
    val fileDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/StockAdminFolder")
    val folderPath = "/storage/emulated/0/Documents/StockAdminFolder"
    var folder = File(folderPath)
    if (!folder.exists()) {
        folder.mkdirs()
    }
    var file = File(fileDir, fileName)


    if (orderHistoryState.orderHistory.isNotEmpty() && !loading) {
        if (!fileSaved.value) {
            dataClassToExcel(
                orderHistoryState.orderHistory,
                file,
                openFileLauncher,
                context,
                fileDir
            )
            Toast.makeText(
                context,
                "Order_History_$formatted.xlsx saved in documents folder",
                Toast.LENGTH_SHORT
            ).show()
            outputmessage = "Order_History_$formatted.xlsx saved in documents folder"
            fileSaved.value = true
        }
    } else {
        fileSaved.value=false
        outputmessage = "No record Available"
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var selected by remember { mutableStateOf("Date") }
        Row(
            Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(
                modifier = Modifier.width(100.dp),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                onClick = { selected = "Date" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == "Date") PrimaryLight else Color.White,
                    contentColor = PrimaryText
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = "Date")
            }
            Button(
                modifier = Modifier.width(100.dp),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                onClick = { selected = "Order ID" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == "Order ID") PrimaryLight else Color.White,
                    contentColor = PrimaryText
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = "Order ID")
            }


        }
        Spacer(modifier = Modifier.size(10.dp))
//        EditBox(selected, categoryViewModel)
        when (selected) {
            "Date" -> OrderHistoryByDate(fileSaved, orderHistoryViewmodel, context)
            "Order ID" -> OrderHistoryByOrderId(fileSaved, orderHistoryViewmodel, context)

        }


        Spacer(modifier = Modifier.size(10.dp))
        if (loading) {
            androidx.compose.material.CircularProgressIndicator(color = PrimaryDark)
        } else {
            if (orderHistoryState.orderHistory.isNotEmpty()) {
//                TableOrderHistory(orderHistory = orderHistoryState.orderHistory)
                try {
                    val uri = Uri.fromFile(file)
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, getMimeType(file.absolutePath))
                        flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }

                    val contentUri = getUriForFile(
                        context,
                        "com.devs.adminapplication.fileprovider",
                        file
                    )
                    intent.setDataAndType(contentUri, getMimeType(file.absolutePath))
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

//                        openFileLauncher.launch(intent)


                } catch (e: IOException) {
//                        setErrorMessage("Error opening file")
                } catch (e: ActivityNotFoundException) {

                }
            }
        }
        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = outputmessage,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable {
                if (outputmessage.equals("Order_History_$formatted.xlsx saved in documents folder")) {
                    try {
                        val uri = Uri.fromFile(file)
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, getMimeType(file.absolutePath))
                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }

                        val contentUri = getUriForFile(
                            context,
                            "com.devs.adminapplication.fileprovider",
                            file
                        )
                        intent.setDataAndType(contentUri, getMimeType(file.absolutePath))
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        openFileLauncher.launch(intent)
                    } catch (e: IOException) {
//                        setErrorMessage("Error opening file")
                    } catch (e: ActivityNotFoundException) {

                    }
                }

            })

    }
}

@Composable
fun OrderHistoryByOrderId(
    fileSaved: MutableState<Boolean>,
    orderHistoryViewmodel: OrderHistoryViewmodel, context: Context) {
    val fromOrderID = remember { mutableStateOf("") }
    val toOrderID = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    TextBox(fromOrderID, "From Order ID", focusManager, enabled = true)
    TextBox(toOrderID, "To Order ID", focusManager, enabled = true)
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .height(55.dp),
        shape = RoundedCornerShape(4.dp),
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryLight
        ),
        onClick = {
            fileSaved.value = false

            if (fromOrderID != null && toOrderID != null) {
                val res = toOrderID.value.toInt() - fromOrderID.value.toInt()
                if (res >= 0) {
                    orderHistoryViewmodel.getOrderHistoryByOrderId(fromOrderID.value,toOrderID.value)

                } else {
                    Toast.makeText(
                        context,
                        "To Order Id is less than to From Order ID ",
                        Toast.LENGTH_SHORT
                    ).show()
                    orderHistoryViewmodel.resetOrderHistory()
                }
            }


        }
    ) {
        Text(
            text = "Fetch History",
            color = Color.Black,
            fontSize = 17.sp
        )
    }
}

@Composable
private fun OrderHistoryByDate(
    fileSaved: MutableState<Boolean>,
    orderHistoryViewmodel: OrderHistoryViewmodel,
    context: Context,
) {

    val date1text = remember { mutableStateOf("") }
    val date2text = remember { mutableStateOf("") }
    var date1 by remember {
        mutableStateOf(LocalDate.now())
    }
    var date2 by remember {
        mutableStateOf(LocalDate.now())
    }
    DateBoxSelectable(dateText = date1text, label = "From Date") {
        date1 = it
    }
    DateBoxSelectable(dateText = date2text, label = "To Date") {
        date2 = it
    }
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .height(55.dp),
        shape = RoundedCornerShape(4.dp),
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryLight
        ),
        onClick = {
            fileSaved.value = false
            Log.d(
                "json2xls",
                "OrderHistoryScreen: ${date1text.value} $date1 ${date2text.value} $date2"
            )
            if (date1 != null && date2 != null) {
                val res = date1!!.compareTo(date2)
                if (res < 0) {
                    orderHistoryViewmodel.getOrderHistory(date1text.value, date2text.value)

                } else if (res > 0) {
                    Toast.makeText(context, "To Date is before From Date ", Toast.LENGTH_SHORT)
                        .show()
//                    outputmessage1 = "To Date is before From Date "
                    orderHistoryViewmodel.resetOrderHistory()
                } else {
                    Toast.makeText(
                        context,
                        "To Date is equal to From Date ",
                        Toast.LENGTH_SHORT
                    ).show()
//                    outputmessage1 = "To Date is equal to From Date "
                    orderHistoryViewmodel.resetOrderHistory()
                }
            }


        }
    ) {
        Text(
            text = "Fetch History",
            color = Color.Black,
            fontSize = 17.sp
        )
    }

}

fun getMimeType(filePath: String): String {
    val ext = filePath.substringAfterLast('.')
    return when (ext.toLowerCase()) {
        "pdf" -> "application/pdf"
        "doc", "docx" -> "application/msword"
        "xls", "xlsx" -> "application/vnd.ms-excel"
        "ppt", "pptx" -> "application/vnd.ms-powerpoint"
        "txt" -> "text/plain"
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        else -> "*/*"
    }
}

data class OrderHistory(
    val orderId: Int,
    val userId: String,
    val totalAmount: Double,
    val color: String,
    val size: String,
    val quantity: Int,
    val address: Address,
    val productName: String
)

data class Address(
    val orderId: Int = 0,
    val name: String = "",
    val address1: String = "",
    val address2: String = "",
    val city: String = "",
    val state: String = "",
    val mobileNo: String = "",
    val pincode: String = "",
    val paymentAmount: String = "",
    val paymentType: String = ""
)

data class OrderHistoryResponse(
    val errorCode: String,
    val errorMessage: String,
    val orderHistory: List<OrderHistory>
)


fun dataClassToExcel(
    orderHistoryList: List<OrderHistory>,
    file: File,
    openFileLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    context: Context,
    fileDir: File
) {

    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet("Order History")

    // Create header row
    val headerRow = sheet.createRow(0)
    headerRow.createCell(0).setCellValue("Order ID")
    headerRow.createCell(1).setCellValue("User ID")
    headerRow.createCell(2).setCellValue("Total Amount")
    headerRow.createCell(3).setCellValue("Color")
    headerRow.createCell(4).setCellValue("Size")
    headerRow.createCell(5).setCellValue("Quantity")
    headerRow.createCell(6).setCellValue("Product Name")
    headerRow.createCell(7).setCellValue("Name")
    headerRow.createCell(8).setCellValue("Address 1")
    headerRow.createCell(9).setCellValue("Address 2")
    headerRow.createCell(10).setCellValue("City")
    headerRow.createCell(11).setCellValue("State")
    headerRow.createCell(12).setCellValue("Mobile Number")
    headerRow.createCell(13).setCellValue("Pincode")
    headerRow.createCell(14).setCellValue("Payment Type")
    headerRow.createCell(15).setCellValue("Payment Amount")

    // Create data rows
    for (i in orderHistoryList.indices) {
        val order = orderHistoryList[i]
        val dataRow = sheet.createRow(i + 1)
        dataRow.createCell(0).setCellValue(order.orderId.toDouble())
        dataRow.createCell(1).setCellValue(order.userId)
        dataRow.createCell(2).setCellValue(order.totalAmount)
        dataRow.createCell(3).setCellValue(order.color)
        dataRow.createCell(4).setCellValue(order.size)
        dataRow.createCell(5).setCellValue(order.quantity.toDouble())
        dataRow.createCell(6).setCellValue(order.productName)
        if (order.address != null) {
            dataRow.createCell(7).setCellValue(order.address.name)
            dataRow.createCell(8).setCellValue(order.address.address1)
            dataRow.createCell(9).setCellValue(order.address.address2)
            dataRow.createCell(10).setCellValue(order.address.city)
            dataRow.createCell(11).setCellValue(order.address.state)
            dataRow.createCell(12).setCellValue(order.address.mobileNo)
            dataRow.createCell(13).setCellValue(order.address.pincode)
            dataRow.createCell(14).setCellValue(order.address.paymentType)
            dataRow.createCell(15).setCellValue(order.address.paymentAmount)
        }
    }

    // Write the workbook to an Excel file
    val fileOut = FileOutputStream(file.absolutePath)
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()


//    try {
//        val uri = Uri.fromFile(file)
//        val intent = Intent(Intent.ACTION_VIEW).apply {
//            setDataAndType(uri, getMimeType(file.absolutePath))
//            flags =
//                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
//        }
//
//        val contentUri = getUriForFile(
//            context,
//            "com.devs.adminapplication.fileprovider",
//            file
//        )
//        intent.setDataAndType(contentUri, getMimeType(file.absolutePath))
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//
//            openFileLauncher.launch(intent)
//
//
//    } catch (e: IOException) {
////                        setErrorMessage("Error opening file")
//    } catch (e: ActivityNotFoundException) {
//
//    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateBoxSelectable(
    dateText: MutableState<String>,
    label: String,
    enabled: Boolean = true,
    isError: MutableState<Boolean> = mutableStateOf(false),
    onValueChange: (id: LocalDate) -> Unit = { }
) {

    var textfieldSize by remember {
        mutableStateOf(Size.Zero)
    }
    val calendarState = rememberSheetState()
//    val suggestions = listOf("Kotlin", "Java", "Dart", "Python")
    Column() {
        OutlinedTextField(
            value = dateText.value,
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

            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF00BCD4),
                cursorColor = Color(0xFF00BCD4),
                focusedLabelColor = Color(0xFF00ACC1)
            ),
            trailingIcon = {
                androidx.compose.material.Icon(Icons.Outlined.CalendarMonth,
                    "contentDescription",
                    Modifier.clickable { calendarState.show() })
            },
            enabled = enabled,
            isError = isError.value,
            supportingText = {
                if (isError.value)
                    Text(text = "*Required")
            }
        )

        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                yearSelection = true,
                monthSelection = true,
                style = CalendarStyle.MONTH
            ),
            selection = CalendarSelection.Date { date ->

                Log.d("json2xls", date.toString())
                dateText.value = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date)
                onValueChange(date)
            },
        )
    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun TableOrderHistory(
    orderHistory: List<OrderHistory>,

    ) {


//    Column(
//        modifier = Modifier
//            .border(
//                width = 1.dp,
//                color = androidx.compose.material.TextFieldDefaults
//                    .outlinedTextFieldColors()
//                    .placeholderColor(
//                        enabled = true
//                    ).value,
//                shape = RoundedCornerShape(5.dp)
//            )
//            .padding(5.dp)
//    ) {
//        Row {
//            cell("Order ID", width = 60.dp, saveEnabled = true)
//            cell("User ID", width = 60.dp, saveEnabled = true)
//            cell("Total", width = 60.dp, saveEnabled = true)
//            cell("Color", width = 60.dp, saveEnabled = true)
//            cell("Size", width = 60.dp, saveEnabled = true)
//            cell("Quantity", width = 60.dp, saveEnabled = true)
//        }
//
//        for (order in orderHistory) {
//            Row() {
//                cell(order.orderId.toString(), width = 60.dp, saveEnabled = true)
//                cell(order.userId.toString(), width = 60.dp, saveEnabled = true)
//                cell(order.totalAmount.toString(), width = 60.dp, saveEnabled = true)
//                cell(order.color.toString(), width = 60.dp, saveEnabled = true)
//                cell(order.size.toString(), width = 60.dp, saveEnabled = true)
//                cell(order.quantity.toString(), width = 60.dp, saveEnabled = true)
//            }
//        }
//        //scrollable row
//    }
    Column(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = androidx.compose.material.TextFieldDefaults
                    .outlinedTextFieldColors()
                    .placeholderColor(
                        enabled = true
                    ).value,
                shape = RoundedCornerShape(5.dp)
            )
            .height(450.dp)
            .padding(5.dp)
    ) {
        Row {
            cell("Order ID", width = 60.dp, saveEnabled = true)
            cell("User ID", width = 60.dp, saveEnabled = true)
            cell("Total", width = 60.dp, saveEnabled = true)
            cell("Color", width = 60.dp, saveEnabled = true)
            cell("Size", width = 60.dp, saveEnabled = true)
            cell("Quantity", width = 60.dp, saveEnabled = true)
        }
        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {

            items(orderHistory) { order ->
                Row() {
                    cell(order.orderId.toString(), width = 60.dp, saveEnabled = true)
                    cell(order.userId.toString(), width = 60.dp, saveEnabled = true)
                    cell(order.totalAmount.toString(), width = 60.dp, saveEnabled = true)
                    cell(order.color.toString(), width = 60.dp, saveEnabled = true)
                    cell(order.size.toString(), width = 60.dp, saveEnabled = true)
                    cell(order.quantity.toString(), width = 60.dp, saveEnabled = true)
                }
            }
        }
    }

}
