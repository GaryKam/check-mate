package com.oukschub.checkmate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oukschub.checkmate.ui.screen.Checklists
import com.oukschub.checkmate.ui.screen.Home
import com.oukschub.checkmate.ui.screen.Profile

object Destinations {
    const val CHECKLISTS_ROUTE = "checklists"
    const val HOME_ROUTE = "home"
    const val PROFILE_ROUTE = "profile"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Destinations.HOME_ROUTE) {
        composable(Destinations.CHECKLISTS_ROUTE) {
            Checklists(
                onNavigateToHome = { navController.navigate(Destinations.HOME_ROUTE) },
                onNavigateToProfile = { navController.navigate(Destinations.PROFILE_ROUTE) }
            )
        }

        composable(Destinations.HOME_ROUTE) {
            Home(
                onNavigateToChecklists = { navController.navigate(Destinations.CHECKLISTS_ROUTE) },
                onNavigateToProfile = { navController.navigate(Destinations.PROFILE_ROUTE) }
            )
        }

        composable(Destinations.PROFILE_ROUTE) {
            Profile(
                onNavigateToChecklists = { navController.navigate(Destinations.CHECKLISTS_ROUTE) },
                onNavigateToHome = { navController.navigate(Destinations.HOME_ROUTE) }
            )
        }
    }
}