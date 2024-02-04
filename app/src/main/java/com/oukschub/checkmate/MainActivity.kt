package com.oukschub.checkmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.oukschub.checkmate.ui.theme.AppTheme
import com.oukschub.checkmate.util.CustomTree
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Activity to display the main composable.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(CustomTree("fiefie:"))
        }

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
            )
        }

        setContent {
            AppTheme(useDarkTheme = false) {
                CheckMateApp()
            }
        }
    }
}
