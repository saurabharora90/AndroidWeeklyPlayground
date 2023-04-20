package com.example.androidweeklyplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.ui.theme.AndroidWeeklyPlaygroundTheme
import com.example.androidweeklyplayground.weekly_566.AccountInfo
import com.example.androidweeklyplayground.weekly_566.GoogleAccountSwitcherWithPointerInput
import com.example.androidweeklyplayground.weekly_566.GoogleAccountSwitcherWithSwipeable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val accountInfo = listOf(
            AccountInfo(R.drawable.horse),
            AccountInfo(R.drawable.dog),
            AccountInfo(R.drawable.cat),
        )
        setContent {
            AndroidWeeklyPlaygroundTheme(darkTheme = true) {
                Home(accountInfo)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(accountInfos: List<AccountInfo>) {
    // A surface container using the 'background' color from the theme
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(topBar = {
            var currentlySelectedAccountIndex by remember { mutableStateOf(0) }
            GoogleAccountSwitcherWithSwipeable(
                accountInfo = accountInfos[currentlySelectedAccountIndex],
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                onNextAccount = {
                    if (currentlySelectedAccountIndex == accountInfos.size - 1) {
                        currentlySelectedAccountIndex = 0
                    } else {
                        currentlySelectedAccountIndex++
                    }
                },
                onPreviousAccount = {
                    if (currentlySelectedAccountIndex == 0) {
                        currentlySelectedAccountIndex = accountInfos.size - 1
                    } else {
                        currentlySelectedAccountIndex--
                    }
                }
            )
        }, content = {
            var currentlySelectedAccountIndex by remember { mutableStateOf(0) }
            GoogleAccountSwitcherWithPointerInput(
                accountInfo = accountInfos[currentlySelectedAccountIndex], modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                if (currentlySelectedAccountIndex == accountInfos.size - 1) {
                    currentlySelectedAccountIndex = 0
                } else {
                    currentlySelectedAccountIndex++
                }
            }
        })
    }
}