package com.oukschub.checkmate.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.oukschub.checkmate.component.BottomNavBar

@Composable
fun Profile(onNavigateToHome: () -> Unit, onNavigateToChecklists: () -> Unit) {
    BottomNavBar(
        onNavigateToHome = { onNavigateToHome() },
        onNavigateToProfile = { /*TO-DO*/ },
        onNavigateToChecklists = { onNavigateToChecklists() }
    ) {
        Column {
            Text(text = "Profile")
        }
    }
}