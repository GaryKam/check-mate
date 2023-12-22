package com.oukschub.checkmate.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Home(onNavigateToProfile: () -> Unit, onNavigateToChecklists: () -> Unit) {
    Column {
        Text(text = "Home")

        Button(onClick = {
            onNavigateToProfile()
        }) {
            Text(text = "To Profile")
        }

        Button(onClick = {
            onNavigateToChecklists()
        }) {
            Text(text = "To Checklists")
        }
    }
}