package com.oukschub.checkmate.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.oukschub.checkmate.ui.addchecklist.AddChecklistScreen
import com.oukschub.checkmate.ui.checklistdetail.ChecklistDetailScreen
import com.oukschub.checkmate.ui.checklists.ChecklistsScreen
import com.oukschub.checkmate.ui.checklists.ChecklistsViewModel
import com.oukschub.checkmate.ui.forgotpassword.ForgotPasswordScreen
import com.oukschub.checkmate.ui.home.HomeScreen
import com.oukschub.checkmate.ui.home.HomeViewModel
import com.oukschub.checkmate.ui.profile.ProfileScreen
import com.oukschub.checkmate.ui.profile.ProfileViewModel
import com.oukschub.checkmate.ui.signin.SignInScreen
import com.oukschub.checkmate.ui.signup.SignUpScreen
import com.oukschub.checkmate.util.FirebaseUtil

/**
 * Hosts the content of each screen being displayed.
 */
@Composable
fun CheckMateNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = if (FirebaseUtil.isSignedIn()) Screen.Home.route else Screen.SignIn.route,
    navController: NavHostController = rememberNavController(),
    checklistsViewModel: ChecklistsViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.SignIn.route) {
            SignInScreen(
                onSignIn = {
                    navController.popBackStack()
                    navController.navigate(Screen.Home.route)
                },
                onForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onFooterClick = { navController.navigate(Screen.SignUp.route) },
            )
        }

        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                onSignUp = {
                    navController.popBackStack(Screen.SignIn.route, true)
                    navController.navigate(Screen.Home.route)
                },
                onFooterClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Checklists.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screen.Home.route, Screen.Profile.route -> slideScreenIn(fromRight = false)
                    else -> EnterTransition.None
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screen.Home.route, Screen.Profile.route -> slideScreenOut(toRight = false)
                    else -> ExitTransition.None
                }
            }
        ) {
            ChecklistsScreen(
                viewModel = checklistsViewModel,
                onChecklistClick = { checklistIndex ->
                    navController.navigate("${Screen.ChecklistDetail.route}/$checklistIndex")
                }
            )
        }

        composable(
            route = Screen.Home.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screen.Checklists.route -> slideScreenIn(fromRight = true)
                    Screen.Profile.route -> slideScreenIn(fromRight = false)
                    else -> EnterTransition.None
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screen.Checklists.route -> slideScreenOut(toRight = true)
                    Screen.Profile.route -> slideScreenOut(toRight = false)
                    else -> ExitTransition.None
                }
            }
        ) {
            HomeScreen(viewModel = homeViewModel)
        }

        composable(
            route = Screen.Profile.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screen.Checklists.route, Screen.Home.route -> slideScreenIn(fromRight = true)
                    else -> EnterTransition.None
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screen.Checklists.route, Screen.Home.route -> slideScreenOut(toRight = true)
                    else -> ExitTransition.None
                }
            }
        ) {
            ProfileScreen(
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

        composable(
            route = Screen.AddChecklist.route,
            enterTransition = { slideInVertically() },
            exitTransition = { fadeOut() + slideOutVertically() }
        ) {
            AddChecklistScreen(
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        val checklistIndexKey = "checklistIndex"
        composable(
            route = "${Screen.ChecklistDetail.route}/{$checklistIndexKey}",
            arguments = listOf(navArgument(checklistIndexKey) { type = NavType.IntType }),
            enterTransition = { scaleIn() },
            exitTransition = { fadeOut() + scaleOut() }
        ) { backStackEntry ->
            ChecklistDetailScreen(
                checklistIndex = backStackEntry.arguments?.getInt(checklistIndexKey)!!,
                onBack = { navController.popBackStack() },
                onDelete = { navController.popBackStack() }
            )
        }
    }
}

private fun slideScreenIn(fromRight: Boolean): EnterTransition {
    return slideInHorizontally(initialOffsetX = { if (fromRight) it else -it })
}

private fun slideScreenOut(toRight: Boolean): ExitTransition {
    return slideOutHorizontally(targetOffsetX = { if (toRight) it else -it })
}
