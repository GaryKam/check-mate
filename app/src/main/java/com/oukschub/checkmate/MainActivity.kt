package com.oukschub.checkmate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oukschub.checkmate.screen.Checklists
import com.oukschub.checkmate.screen.Home
import com.oukschub.checkmate.screen.Profile

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    Home(
                        onNavigateToProfile = { navController.navigate("profile") },
                        onNavigateToChecklists = { navController.navigate("checklists") }
                    )
                }

                composable("profile") {
                    Profile(
                        onNavigateToHome = { navController.navigate("home") },
                        onNavigateToChecklists = { navController.navigate("checklists") },
                    )
                }

                composable("checklists") {
                    Checklists(
                        onNavigateToHome = { navController.navigate("home") },
                        onNavigateToProfile = { navController.navigate("profile") }
                    )
                }
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}