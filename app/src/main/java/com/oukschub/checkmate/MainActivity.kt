package com.oukschub.checkmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.navigation.CheckMateApp
import com.oukschub.checkmate.ui.theme.CheckMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CheckMateTheme(darkTheme = false) {
                FirebaseAuth.getInstance().signOut()
                CheckMateApp()
            }
        }
    }
}
