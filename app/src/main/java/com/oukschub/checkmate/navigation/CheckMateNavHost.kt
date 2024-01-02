package com.oukschub.checkmate.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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

@Composable
fun CheckMateNavHost(
    startDestination: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.SignIn.route) {
            SignIn(onSignIn = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) } })
        }

        composable(
            route = Screen.Checklists.route,
            enterTransition = { slideScreenIn(true) },
            exitTransition = { slideScreenOut(false) }
        ) {
            Checklists()
        }

        composable(
            route = Screen.Home.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screen.Checklists.route -> slideScreenIn(false)
                    Screen.Profile.route -> slideScreenIn(true)
                    else -> EnterTransition.None
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screen.Checklists.route -> slideScreenOut(true)
                    Screen.Profile.route -> slideScreenOut(false)
                    else -> ExitTransition.None
                }
            }
        ) {
            Home()
        }

        composable(route = Screen.Profile.route,
            enterTransition = { slideScreenIn(false) },
            exitTransition = { slideScreenOut(true) }
        ) {
            Profile(onSignOut = { navController.navigate(Screen.SignIn.route) })
        }

        composable(Screen.CreateChecklist.route) { CreateChecklist() }
    }
}

private fun slideScreenIn(fromRight: Boolean): EnterTransition {
    return slideInHorizontally(initialOffsetX = { if (fromRight) -it else it })
}

private fun slideScreenOut(toRight: Boolean): ExitTransition {
    return slideOutHorizontally(targetOffsetX = { if (toRight) it else -it })
}