package com.oukschub.checkmate.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.oukschub.checkmate.component.BottomNavBar

@Composable
fun Checklists(onNavigateToHome: () -> Unit, onNavigateToProfile: () -> Unit) {
    BottomNavBar(
        onNavigateToHome = { onNavigateToHome() },
        onNavigateToProfile = { onNavigateToProfile() },
        onNavigateToChecklists = { /*TO-DO*/ }
    ) {
        Column {
            Text(text = "Checklists")
        }
    }
}