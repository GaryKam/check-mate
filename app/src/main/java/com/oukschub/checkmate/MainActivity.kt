package com.oukschub.checkmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.oukschub.checkmate.ui.theme.CheckMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CheckMateTheme(darkTheme = false) {
                CheckMateApp()
            }
        }
    }
}
