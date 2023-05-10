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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.androidweeklyplayground.ui.theme.AndroidWeeklyPlaygroundTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.utils.startDestination
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scaffoldState = rememberScaffoldState(drawerState = drawerState)
            val sheetState =
                rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()
            val currentDestination = navController.appCurrentDestinationAsState().value
                ?: NavGraphs.root.startDestination
            val shouldShowDrawer = remember(currentDestination) {
                currentDestination == NavGraphs.root.startDestination
            }

            AndroidWeeklyPlaygroundTheme {

                ModalBottomSheetLayout(
                    sheetContent = { ThemeSelectorBottomSheet() },
                    sheetState = sheetState,
                    sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                ) {
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
                                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                                )
                            }
                        },
                        drawerContent = {
                            Drawer {
                                coroutineScope.launch {
                                    drawerState.close()
                                    sheetState.show()
                                }
                            }
                        },
                        drawerGesturesEnabled = shouldShowDrawer,
                    ) { paddingValues ->
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            modifier = Modifier.padding(paddingValues),
                            navController = navController
                        )
                    }
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
            .padding(vertical = 64.dp, horizontal = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .clickable(onClick = onChangeThemeClicked),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Theme",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = "Test",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alignByBaseline()
            )
        }
    }
}