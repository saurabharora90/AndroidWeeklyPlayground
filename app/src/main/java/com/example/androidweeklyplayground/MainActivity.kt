package com.example.androidweeklyplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()
            val currentDestination = navController.appCurrentDestinationAsState().value
                ?: NavGraphs.root.startDestination
            val shouldShowDrawer = remember(currentDestination) {
                currentDestination == NavGraphs.root.startDestination
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
                                            if (scaffoldState.drawerState.isClosed)
                                                scaffoldState.drawerState.open()
                                            else
                                                scaffoldState.drawerState.close()
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

                    },
                    drawerGesturesEnabled = shouldShowDrawer
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