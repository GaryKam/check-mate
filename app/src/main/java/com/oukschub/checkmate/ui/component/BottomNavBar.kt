package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BottomNavBar(
    onClickChecklists: () -> Unit,
    onClickHome: () -> Unit,
    onClickProfile: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(actions = {
                NavigationBarItem(
                    selected = false,
                    onClick = { onClickChecklists() },
                    icon = { Icon(Icons.Default.List, contentDescription = "Checklists") },
                    label = { Text(text = "Checklists") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { onClickHome() },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(text = "Home") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { onClickProfile() },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text(text = "Profile") }
                )
            })
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}