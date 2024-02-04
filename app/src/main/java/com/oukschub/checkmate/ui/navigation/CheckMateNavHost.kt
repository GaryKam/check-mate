package com.oukschub.checkmate.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oukschub.checkmate.ui.checklists.Checklists
import com.oukschub.checkmate.ui.checklists.ChecklistsViewModel
import com.oukschub.checkmate.ui.createchecklist.CreateChecklist
import com.oukschub.checkmate.ui.createchecklist.CreateChecklistViewModel
import com.oukschub.checkmate.ui.home.Home
import com.oukschub.checkmate.ui.home.HomeViewModel
import com.oukschub.checkmate.ui.profile.Profile
import com.oukschub.checkmate.ui.profile.ProfileViewModel
import com.oukschub.checkmate.ui.signin.SignIn
import com.oukschub.checkmate.ui.signup.SignUp
import com.oukschub.checkmate.ui.splash.Splash
import com.oukschub.checkmate.util.FirebaseUtil

/**
 * Hosts the content of each screen being displayed.
 */
@Composable
fun CheckMateNavHost(
    startDestination: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    checklistViewModel: ChecklistsViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    createChecklistViewModel: CreateChecklistViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            Splash(
                onComplete = {
                    navController.popBackStack()
                    navController.navigate(if (FirebaseUtil.isSignedIn()) Screen.Home.route else Screen.SignIn.route)
                }
            )
        }

        composable(Screen.SignIn.route) {
            SignIn(
                onSignIn = {
                    navController.popBackStack()
                    navController.navigate(Screen.Home.route)
                },
                onFooterClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUp(
                onSignUp = {
                    navController.popBackStack(Screen.SignIn.route, true)
                    navController.navigate(Screen.Home.route)
                },
                onFooterClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Checklists.route,
            enterTransition = { slideScreenIn(true) },
            exitTransition = { slideScreenOut(false) }
        ) {
            Checklists(viewModel = checklistViewModel)
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
            Home(viewModel = homeViewModel)
        }

        composable(
            route = Screen.Profile.route,
            enterTransition = { slideScreenIn(false) },
            exitTransition = { slideScreenOut(true) }
        ) {
            Profile(
                viewModel = profileViewModel,
                onSignOut = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.CreateChecklist.route) {
            CreateChecklist(
                viewModel = createChecklistViewModel,
                onBack = { navController.popBackStack() },
                onChecklistCreate = { navController.navigate(Screen.Home.route) }
            )
        }
    }
}

private fun slideScreenIn(fromRight: Boolean): EnterTransition {
    return slideInHorizontally(initialOffsetX = { if (fromRight) -it else it })
}

private fun slideScreenOut(toRight: Boolean): ExitTransition {
    return slideOutHorizontally(targetOffsetX = { if (toRight) it else -it })
}
