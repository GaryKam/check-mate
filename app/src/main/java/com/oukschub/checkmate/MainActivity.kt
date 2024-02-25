package com.oukschub.checkmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
            AppTheme(useDarkTheme = false) {
                CheckMateApp()
            }
        }
    }
}
