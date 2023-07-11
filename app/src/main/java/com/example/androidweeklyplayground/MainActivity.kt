package com.example.androidweeklyplayground

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.androidweeklyplayground.notification.createNotification
import com.example.androidweeklyplayground.ui.theme.AndroidWeeklyPlaygroundTheme
import com.example.androidweeklyplayground.ui.theme.AppThemeState
import com.example.androidweeklyplayground.ui.theme.rememberAppThemeState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.utils.startDestination
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
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
                ModalNavigationDrawer(
                    drawerContent = { Drawer { showBottomSheet = true } },
                    drawerState = drawerState,
                    gesturesEnabled = shouldShowDrawer,
                ) {
                    Scaffold(
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
                    ) { paddingValues ->
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            modifier = Modifier.padding(paddingValues),
                            navController = navController
                        )

                    }
                }

                if (showBottomSheet) {
                    ThemeSelectorBottomSheet(sheetState = sheetState) { showBottomSheet = false }
                }
            }
        }
    }
}

@Composable
fun Drawer(
    context: Context = LocalContext.current,
    appThemeState: AppThemeState = rememberAppThemeState(),
    onChangeThemeClicked: () -> Unit
) {
    ModalDrawerSheet {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
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
                text = appThemeState.currentTheme.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alignByBaseline()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    context.createNotification(
                        "Custom Notification",
                        "Checkout this custom notification layout that we'll screenshot test",
                        R.drawable.scenery
                    )
                }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Post Notification",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}