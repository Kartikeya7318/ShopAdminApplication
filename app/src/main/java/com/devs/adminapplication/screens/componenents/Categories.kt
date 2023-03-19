package com.devs.adminapplication.screens.componenents

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.devs.adminapplication.models.util.ChipList

import com.devs.adminapplication.ui.theme.PrimaryLight

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Categories(
    selected: String,
    categoriesList: List<ChipList>?= emptyList(),
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 10.dp, top = 5.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        if (categoriesList != null) {
            for (category in categoriesList) {
                Spacer(modifier = Modifier.width(10.dp))

                FilterChip(
                    selected = selected == category.id.toString(),
                    elevation = FilterChipDefaults.filterChipElevation(4.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryLight,
                        containerColor = Color.White
                    ),
                    onClick = { onClick(category.id.toString()) },
                    border = FilterChipDefaults.filterChipBorder(Color.Transparent, borderWidth = 0.dp),
                    label = { Text(text = category.name,) }
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
    }
}
