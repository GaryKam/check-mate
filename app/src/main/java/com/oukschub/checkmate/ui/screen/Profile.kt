package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        ) {
            val context = LocalContext.current
            Text(text = "Profile")
            Column(
                modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max)
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Text(text = "Change Password")
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Settings")
                }
                Button(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        context.startActivity(SignInActivity.createIntent(context))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Sign Out")
                }
            }

        }
    }
}