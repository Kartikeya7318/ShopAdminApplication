package com.devs.adminapplication.screens.componenents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.toSize
import com.devs.adminapplication.models.util.ChipList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextBox(
    name: MutableState<String>,
    label: String,
    focusManager: FocusManager,
    enabled: Boolean = true,
    isError: MutableState<Boolean> = mutableStateOf(false),
    regex: Regex= Regex("^[a-zA-Z0-9 ]+$")
) {
    OutlinedTextField(
        value = name.value,
        onValueChange = {
            name.value = it
            isError.value = false
        },
        label = { Text(text = label) },
        modifier = Modifier
            .fillMaxWidth(),
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
            focusedLabelColor = Color(0xFF00ACC1),

            ),
        enabled = enabled,
        isError = isError.value || (!name.value.matches(regex)&&name.value.isNotEmpty()),
        supportingText = {
            if (isError.value)
                Text(text = "*Required")
            else if (!name.value.matches(regex)&&name.value.isNotEmpty())
                Text(text = "Invalid Input")
        },

    )

}

fun isInteger(s: String): Boolean {
    return try {
        s.toInt()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextBoxSelectable(
    name: MutableState<String>,
    label: String,
    focusManager: FocusManager,
    expanded: MutableState<Boolean>,
    suggestions: MutableList<ChipList>,
    enabled: Boolean = true,
    isError: MutableState<Boolean> = mutableStateOf(false),
    onValueChange: (id: String) -> Unit = { }
) {
    val icon = if (expanded.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    var textfieldSize by remember {
        mutableStateOf(Size.Zero)
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
                if (enabled)
                    Icon(icon, "contentDescription",
                        Modifier.clickable { expanded.value = !expanded.value })
            },
            enabled = enabled,
            isError = isError.value,
            supportingText = {
                if (isError.value)
                    Text(text = "*Required")
            }
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
                    isError.value = false
                }
            }
        }
    }

}