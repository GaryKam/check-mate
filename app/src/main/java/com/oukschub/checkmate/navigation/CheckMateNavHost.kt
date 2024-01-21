package com.oukschub.checkmate.navigation

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
import com.oukschub.checkmate.ui.screen.Checklists
import com.oukschub.checkmate.ui.screen.CreateChecklist
import com.oukschub.checkmate.ui.screen.Home
import com.oukschub.checkmate.ui.screen.Profile
import com.oukschub.checkmate.ui.screen.SignIn
import com.oukschub.checkmate.ui.screen.SignUp
import com.oukschub.checkmate.ui.screen.Splash
import com.oukschub.checkmate.util.FirebaseUtil
import com.oukschub.checkmate.viewmodel.ChecklistsViewModel
import com.oukschub.checkmate.viewmodel.CreateChecklistViewModel
import com.oukschub.checkmate.viewmodel.HomeViewModel
import com.oukschub.checkmate.viewmodel.ProfileViewModel
import com.oukschub.checkmate.viewmodel.SignInViewModel
import com.oukschub.checkmate.viewmodel.SignUpViewModel

@Composable
fun CheckMateNavHost(
    startDestination: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    signInViewModel: SignInViewModel = hiltViewModel(),
    signUpViewModel: SignUpViewModel = hiltViewModel(),
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
                viewModel = signInViewModel,
                onSignIn = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) } },
                onFooterClick = { navController.navigate(Screen.SignUp.route) }
            )
        }

        composable(Screen.SignUp.route) {
            SignUp(
                viewModel = signUpViewModel,
                onSignUp = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) } },
                onFooterClick = { navController.navigate(Screen.SignIn.route) }
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
                onSignOut = { navController.navigate(Screen.SignIn.route) }
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
