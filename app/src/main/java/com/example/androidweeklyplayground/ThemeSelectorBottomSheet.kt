package com.example.androidweeklyplayground

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.ui.theme.AppThemeState
import com.example.androidweeklyplayground.ui.theme.rememberAppThemeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectorBottomSheet(
    sheetState: SheetState,
    modifier: Modifier = Modifier,
    appThemeState: AppThemeState = rememberAppThemeState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) {
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
        AppThemeState.SupportedTheme.values().forEach {
            ThemeOption(it.name, appThemeState.currentTheme == it, paddingModifier) {
                coroutineScope.launch {
                    appThemeState.updateTheme(it)
                }
            }
        }
    }
}

@Composable
private fun ThemeOption(
    option: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = option)
        RadioButton(selected = isSelected, onClick = onClick)
    }
}