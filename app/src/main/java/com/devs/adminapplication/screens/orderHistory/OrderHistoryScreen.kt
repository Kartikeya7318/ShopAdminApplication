package com.devs.adminapplication.screens.orderHistory

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext

import com.devs.adminapplication.utils.Constants
//import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json.JSONObject
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.content.FileProvider.getUriForFile
import com.devs.adminapplication.models.productResponse.ProductDetail
import com.devs.adminapplication.models.util.ChipList
import com.devs.adminapplication.screens.details.cell
import com.devs.adminapplication.screens.details.inputDialogState
import com.devs.adminapplication.ui.theme.PrimaryLight
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(orderHistoryViewmodel: OrderHistoryViewmodel) {
    val orderHistoryState by orderHistoryViewmodel.orderHistoryState.collectAsState()
    val context = LocalContext.current
    val date1text = remember { mutableStateOf("") }
    val date2text = remember { mutableStateOf("") }
    var date1 by remember {
        mutableStateOf(LocalDate.now())
    }
    var date2 by remember {
        mutableStateOf(LocalDate.now())
    }
    var outputmessage by remember {
        mutableStateOf("")
    }
    val fileName = "Order_History.xlsx"
    val fileDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(fileDir, fileName)

    file.setReadable(true, false)
    file.setWritable(true, false)
    file.setExecutable(true, false)
    if (orderHistoryState.orderHistory.isNotEmpty()) {
//                            val orderHistoryResponse = parseJson(Constants.ORDERHISTORY)
//                            Log.d(
//                                "json2xls",
//                                "OrderHistoryScreen: " + orderHistoryResponse.toString()
//                            )

        dataClassToExcel(orderHistoryState.orderHistory, file.absolutePath)
        Log.d("json2xls", "Path: " + file.absolutePath)
        Toast.makeText(
            context,
            "Order_History.xlsx downloaded",
            Toast.LENGTH_SHORT
        ).show()
        outputmessage = "Order_History.xlsx downloaded"
    } else {
        outputmessage = "No record Available"
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                        outputmessage = "To Date is before From Date "
                        orderHistoryViewmodel.resetOrderHistory()
                    } else {
                        Toast.makeText(
                            context,
                            "To Date is equal to From Date ",
                            Toast.LENGTH_SHORT
                        ).show()
                        outputmessage = "To Date is equal to From Date "
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
        Spacer(modifier = Modifier.size(10.dp))

//        val orderHistoryResponse = parseJson(Constants.ORDERHISTORY)
//        TableOrderHistory(orderHistory = orderHistoryResponse.orderHistory)
        if (orderHistoryState.orderHistory.isNotEmpty()) {
            TableOrderHistory(orderHistory = orderHistoryState.orderHistory)
        }
        Spacer(modifier = Modifier.size(10.dp))
        val openFileLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                // Handle success
            } else {
                Toast.makeText(context, "Error opening file", Toast.LENGTH_SHORT).show()
            }
        }
        Text(
            text = outputmessage,
            modifier = Modifier.clickable {
                if (outputmessage.equals("Order_History.xlsx downloaded")) {
                    try {
                        val uri = Uri.fromFile(file)
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, getMimeType(file.absolutePath))
                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            val contentUri = getUriForFile(context,"com.devs.adminapplication.fileprovider",file)
                            intent.setDataAndType(contentUri, getMimeType(file.absolutePath))
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        openFileLauncher.launch(intent)
                    } catch (e: IOException) {
//                        setErrorMessage("Error opening file")
                    } catch (e: ActivityNotFoundException) {

                    }
                }

            })

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
    val quantity: Int
)

data class OrderHistoryResponse(
    val errorCode: String,
    val errorMessage: String,
    val orderHistory: List<OrderHistory>
)

fun parseJson(jsonData: String): OrderHistoryResponse {
    val jsonObject = JSONObject(jsonData)
    val orderHistoryArray = jsonObject.getJSONArray("orderHistory")
    val orderHistory = mutableListOf<OrderHistory>()
    for (i in 0 until orderHistoryArray.length()) {
        val orderObject = orderHistoryArray.getJSONObject(i)
        val order = OrderHistory(
            orderObject.getInt("orderId"),
            orderObject.getString("userId"),
            orderObject.getDouble("totalAmount"),
            orderObject.getString("color"),
            orderObject.getString("size"),
            orderObject.getInt("quantity")
        )
        orderHistory.add(order)
    }
    return OrderHistoryResponse(
        jsonObject.getString("errorCode"),
        jsonObject.getString("errorMessage"),
        orderHistory
    )
}

fun dataClassToExcel(orderHistoryList: List<OrderHistory>, fileName: String) {
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
    }

    // Write the workbook to an Excel file
    val fileOut = FileOutputStream(fileName)
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()
}

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

        for (order in orderHistory) {
            Row() {
                cell(order.orderId.toString(), width = 60.dp, saveEnabled = true)
                cell(order.userId.toString(), width = 60.dp, saveEnabled = true)
                cell(order.totalAmount.toString(), width = 60.dp, saveEnabled = true)
                cell(order.color.toString(), width = 60.dp, saveEnabled = true)
                cell(order.size.toString(), width = 60.dp, saveEnabled = true)
                cell(order.quantity.toString(), width = 60.dp, saveEnabled = true)
            }
        }
        //scrollable row
    }

}
