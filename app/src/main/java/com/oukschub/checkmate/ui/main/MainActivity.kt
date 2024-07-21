package com.oukschub.checkmate.ui.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.oukschub.checkmate.BuildConfig
import com.oukschub.checkmate.CheckMateApp
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
        installSplashScreen().setOnExitAnimationListener { splashScreen ->
            val fadeOut = ObjectAnimator.ofFloat(splashScreen.view, View.ALPHA, 0.0F)
            fadeOut.duration = 250L
            fadeOut.doOnEnd { splashScreen.remove() }
            fadeOut.start()
        }

        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(CustomTree("fiefie:"))
        }

        val viewModel: MainViewModel by viewModels()
        viewModel.loadData()

        setContent {
            AppTheme(useDarkTheme = false) {
                CheckMateApp()
            }
        }

        val content = findViewById<View>(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener { viewModel.isAppReady }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onPause() {
        super.onPause()
        val viewModel: MainViewModel by viewModels()
        viewModel.onPause()
    }
}
