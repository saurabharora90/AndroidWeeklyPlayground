package com.example.androidweeklyplayground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.ThemeSelectorBottomSheet() {
    Text(
        text = "Select Theme",
        modifier = Modifier.padding(start = 8.dp, top = 32.dp),
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(
        modifier = Modifier
            .padding(top = 8.dp)
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )

    val paddingModifier = remember {
        Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
    }
    ThemeOption("Dark", false, paddingModifier)
    ThemeOption("Light", false, paddingModifier)
    ThemeOption("System", true, paddingModifier)
}

@Composable
private fun ThemeOption(option: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = option)
        RadioButton(selected = isSelected, onClick = { /*TODO*/ })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun ThemeSelectorPreview() {
    ModalBottomSheetLayout(
        sheetContent = { ThemeSelectorBottomSheet() },
        sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded),
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {

    }
}