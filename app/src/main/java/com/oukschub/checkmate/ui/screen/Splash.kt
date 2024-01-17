package com.oukschub.checkmate.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.oukschub.checkmate.viewmodel.SplashViewModel

@Composable
fun Splash(
    viewModel: SplashViewModel,
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.runTasks { onNavigate() }
    }

    Text(text = "Splash")
}
