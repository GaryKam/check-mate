package com.oukschub.checkmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.oukschub.checkmate.ui.theme.CheckMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            CheckMateTheme(darkTheme = false) {
                CheckMateApp()
            }
        }
    }
}
