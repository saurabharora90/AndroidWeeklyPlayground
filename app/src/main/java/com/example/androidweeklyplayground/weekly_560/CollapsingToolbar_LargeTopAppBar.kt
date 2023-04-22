package com.example.androidweeklyplayground.weekly_560

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.helpers.ReusableVerticalLazyList
import com.example.androidweeklyplayground.ui.theme.AndroidWeeklyPlaygroundTheme

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CollapsingToolBarLargeTopAppBar(modifier: Modifier = Modifier) {
    val topBarScrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(modifier = modifier, topBar = {
        LargeTopAppBar(
            title = { Text(text = "Android Weekly #560") },
            navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.Call, contentDescription = "Call")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "Location")
                }
            },
            scrollBehavior = topBarScrollBehaviour
        )
    }) {
        ReusableVerticalLazyList(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .nestedScroll(topBarScrollBehaviour.nestedScrollConnection)
        )
    }
}

@Preview
@Composable
fun CollapsingToolbarPreview() {
    AndroidWeeklyPlaygroundTheme {
        CollapsingToolBarLargeTopAppBar()
    }
}