package com.oukschub.checkmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.oukschub.checkmate.ui.theme.CheckMateTheme
import com.oukschub.checkmate.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel by viewModels()
        viewModel.getChecklists {
            setContent {
                CheckMateTheme(darkTheme = false) {
                    CheckMateApp()
                }
            }
        }
    }
}
