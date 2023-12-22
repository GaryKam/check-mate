package com.oukschub.checkmate.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Profile(onNavigateToHome: () -> Unit, onNavigateToChecklists: () -> Unit) {

    Column {
        Text(text = "Profile")

        Button(onClick = {
            onNavigateToHome()
        }) {
            Text(text = "To Home")
        }

        Button(onClick = {
            onNavigateToChecklists()
        }) {
            Text(text = "To Profile")
        }
    }
}