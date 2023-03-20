package com.devs.adminapplication.screens.componenents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TextBox(
    name: MutableState<String>,
    label: String,
    focusManager: FocusManager,
    enabled:Boolean=true

    ) {
    OutlinedTextField(
        value = name.value,
        onValueChange = { name.value = it },
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
            focusedLabelColor = Color(0xFF00ACC1)
        ),
        enabled = enabled
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