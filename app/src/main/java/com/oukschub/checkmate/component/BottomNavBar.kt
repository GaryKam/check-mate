package com.oukschub.checkmate.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BottomNavBar(
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToChecklists: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(actions = {
                IconButton(onClick = { onNavigateToChecklists() }) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = "Checklists"
                    )
                }
                IconButton(onClick = { onNavigateToHome() }) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Home"
                    )
                }
                IconButton(onClick = { onNavigateToProfile() }) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile"
                    )
                }
            })
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}