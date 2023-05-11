package com.example.androidweeklyplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.androidweeklyplayground.ui.theme.AndroidWeeklyPlaygroundTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.utils.startDestination
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scaffoldState = rememberScaffoldState(drawerState = drawerState)
            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()
            val currentDestination = navController.appCurrentDestinationAsState().value
                ?: NavGraphs.root.startDestination
            val shouldShowDrawer = remember(currentDestination) {
                currentDestination == NavGraphs.root.startDestination
            }
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            var showBottomSheet by remember {
                mutableStateOf(false)
            }

            AndroidWeeklyPlaygroundTheme {
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        if (shouldShowDrawer) {
                            TopAppBar(
                                title = { Text(text = "Android Weekly Playground") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        coroutineScope.launch {
                                            if (drawerState.isClosed)
                                                drawerState.open()
                                            else
                                                drawerState.close()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                            )
                        }
                    },
                    drawerContent = {
                        Drawer { showBottomSheet = true }
                    },
                    drawerGesturesEnabled = shouldShowDrawer,
                    drawerBackgroundColor = MaterialTheme.colorScheme.surface,
                    backgroundColor = MaterialTheme.colorScheme.background,
                ) { paddingValues ->
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        modifier = Modifier.padding(paddingValues),
                        navController = navController
                    )
                }

                if (showBottomSheet) {
                    ThemeSelectorBottomSheet(sheetState = sheetState) { showBottomSheet = false }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Drawer(onChangeThemeClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(vertical = 32.dp)
                .clickable(onClick = onChangeThemeClicked)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Theme",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = "Test",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alignByBaseline()
            )
        }
    }
}