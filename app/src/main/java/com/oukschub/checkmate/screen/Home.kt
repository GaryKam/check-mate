package com.oukschub.checkmate.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.oukschub.checkmate.component.BottomNavBar

@Composable
fun Home(onNavigateToProfile: () -> Unit, onNavigateToChecklists: () -> Unit) {
    BottomNavBar(
        onNavigateToHome = { /*TO-DO*/ },
        onNavigateToProfile = { onNavigateToProfile() },
        onNavigateToChecklists = { onNavigateToChecklists() }
    ) {
        Column {
            Text(text = "Home")
        }
    }
}