package com.oukschub.checkmate.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Checklists(onNavigateToHome: () -> Unit, onNavigateToProfile: () -> Unit) {
    
    Column {
        Text(text = "Checklists")

        Button(onClick = {
            onNavigateToHome()
        }) {
            Text(text = "To Home")
        }
        
        Button(onClick = {
            onNavigateToProfile()
        }) {
            Text(text = "To Profile")
        }
    }
}