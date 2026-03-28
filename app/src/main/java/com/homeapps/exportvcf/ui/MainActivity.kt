package com.homeapps.exportvcf.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.homeapps.exportvcf.ui.features.main.MainScreen
import com.homeapps.exportvcf.ui.theme.ExportVCFTheme
import com.homeapps.exportvcf.ui.theme.ThemeViewModel
import io.github.themeanimator.ThemeAnimationFormat
import io.github.themeanimator.ThemeAnimationScope
import io.github.themeanimator.rememberThemeAnimationState
import io.github.themeanimator.theme.isDark

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themeViewModel = ThemeViewModel(appContext = applicationContext)
        enableEdgeToEdge()
        setContent {
            val animationState = rememberThemeAnimationState(
                themeProvider = themeViewModel,
                format = ThemeAnimationFormat.CircularAroundPress
            )
            ThemeAnimationScope(state = animationState) {
                ExportVCFTheme(darkTheme = animationState.uiTheme.isDark()) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MainScreen(
                            animationState = animationState,
                            modifier = Modifier.padding(paddingValues = innerPadding)
                        )
                    }
                }
            }
        }
    }
}
