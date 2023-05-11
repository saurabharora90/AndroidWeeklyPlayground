package com.example.androidweeklyplayground.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private val themeKey = stringPreferencesKey("current_theme")

@Stable
class AppThemeState(
    private val dataStore: DataStore<Preferences>,
    private val isSystemInDarkTheme: Boolean
) {
    enum class SupportedTheme {
        System, Light, Dark
    }

    val currentTheme: SupportedTheme
        @Composable get() = getCurrentTheme().collectAsStateWithLifecycle(initialValue = runBlocking { getInitialTheme() }).value

    val useDarkTheme: Boolean
        @Composable get() = currentTheme.useDarkTheme()


    private fun SupportedTheme.useDarkTheme(): Boolean {
        return when (this) {
            SupportedTheme.System -> isSystemInDarkTheme
            SupportedTheme.Light -> false
            SupportedTheme.Dark -> true
        }
    }

    private fun getCurrentTheme(): Flow<SupportedTheme> {
        return dataStore.data
            .map { it[themeKey] }
            .map { it?.let { SupportedTheme.valueOf((it)) } ?: SupportedTheme.System }
    }

    private suspend fun getInitialTheme(): SupportedTheme {
        return dataStore.data.first()[themeKey]?.let { SupportedTheme.valueOf((it)) }
            ?: SupportedTheme.System
    }

    suspend fun updateTheme(newTheme: SupportedTheme) {
        dataStore.edit {
            it[themeKey] = newTheme.name
        }
    }
}

@Composable
fun rememberAppThemeState(context: Context = LocalContext.current) =
    AppThemeState(context.dataStore, isSystemInDarkTheme())