package com.oukschub.checkmate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oukschub.checkmate.ui.screen.Checklists
import com.oukschub.checkmate.ui.screen.CreateChecklist
import com.oukschub.checkmate.ui.screen.Home
import com.oukschub.checkmate.ui.screen.Profile
import com.oukschub.checkmate.ui.screen.SignIn
import com.oukschub.checkmate.util.FirebaseUtil

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = if (FirebaseUtil.isLoggedIn()) Screen.Home.route else Screen.SignIn.route,
        modifier = modifier
    ) {
        composable(Screen.SignIn.route) {
            SignIn(onSignIn = { navController.navigate(Screen.Home.route) })
        }
        composable(Screen.Checklists.route) { Checklists() }
        composable(Screen.Home.route) { Home() }
        composable(Screen.Profile.route) {
            Profile(onSignOut = { navController.navigate(Screen.SignIn.route) })
        }
        composable(Screen.CreateChecklist.route) { CreateChecklist() }
    }
}
