package com.homeapps.exportvcf.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homeapps.exportvcf.data.datastore.ExportVCFDataStore
import io.github.themeanimator.theme.Theme
import io.github.themeanimator.theme.ThemeProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ThemeViewModel(appContext: Context): ViewModel(), ThemeProvider {
    val dataStore = ExportVCFDataStore(appContext)

    override val currentTheme: StateFlow<Theme> = dataStore.getDarkThemeFlow()
        .map { value ->
            if (value) Theme.Dark else Theme.Light
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Theme.Dark
        )


    override suspend fun updateTheme(theme: Theme) {
        dataStore.setDarkTheme(darkTheme = theme is Theme.Dark)
    }
}