package com.oukschub.checkmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.ui.theme.CheckMateTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: ChecklistRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        repository.loadChecklists {
            setContent {
                CheckMateTheme(darkTheme = false) {
                    CheckMateApp()
                }
            }
        }
    }
}
