package com.oukschub.checkmate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oukschub.checkmate.ui.forgotpassword.ForgotPasswordScreen
import com.oukschub.checkmate.ui.forgotpassword.ForgotPasswordViewModel
import com.oukschub.checkmate.ui.navigation.Screen
import com.oukschub.checkmate.ui.signin.SignInScreen
import com.oukschub.checkmate.ui.signin.SignInViewModel
import com.oukschub.checkmate.ui.signup.SignUpScreen
import com.oukschub.checkmate.ui.signup.SignUpViewModel

@Composable
fun TestCheckMateNavHost(
    startDestination: String,
    signInViewModel: SignInViewModel,
    forgotPasswordViewModel: ForgotPasswordViewModel,
    signUpViewModel: SignUpViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
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
                viewModel = signInViewModel
            )
        }

        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                viewModel = forgotPasswordViewModel
            )
        }

        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                onSignUp = {
                    navController.popBackStack(Screen.SignIn.route, true)
                    navController.navigate(Screen.Home.route)
                },
                onFooterClick = { navController.popBackStack() },
                viewModel = signUpViewModel
            )
        }
    }
}
