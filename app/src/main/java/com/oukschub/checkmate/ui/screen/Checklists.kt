package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oukschub.checkmate.ui.component.BottomNavBar

@Composable
fun Checklists(
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomNavBar(
        onNavigateToChecklists = { /*TO-DO*/ },
        onNavigateToHome = { onNavigateToHome() },
        onNavigateToProfile = { onNavigateToProfile() }
    ) {
        Column {
            Text(text = "Checklists")
        }
    }
}