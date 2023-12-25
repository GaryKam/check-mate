package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.SignInActivity
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
            val context = LocalContext.current
            Text(text = "Profile")
            Button(onClick = {
                FirebaseAuth.getInstance().signOut()
                context.startActivity(SignInActivity.createIntent(context))
            }) {
                Text(text = "Sign Out")
            }
        }
    }
}