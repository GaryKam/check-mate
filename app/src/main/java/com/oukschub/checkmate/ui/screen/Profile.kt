package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oukschub.checkmate.ui.component.BottomNavBar

@Composable
fun Profile(
    onNavigateToChecklists: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomNavBar(
        onNavigateToChecklists = { onNavigateToChecklists() },
        onNavigateToHome = { onNavigateToHome() },
        onNavigateToProfile = { /*TO-DO*/ }
    ) {
        Column {
            Text(text = "Profile")
        }
    }
}