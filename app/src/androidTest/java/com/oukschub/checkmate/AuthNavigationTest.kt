package com.oukschub.checkmate

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.data.repository.UserRepository
import com.oukschub.checkmate.ui.forgotpassword.ForgotPasswordViewModel
import com.oukschub.checkmate.ui.navigation.Screen
import com.oukschub.checkmate.ui.signin.SignInViewModel
import com.oukschub.checkmate.ui.signup.SignUpViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AuthNavigationTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @RelaxedMockK
    private lateinit var checklistRepository: ChecklistRepository

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    private lateinit var context: Context
    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()
        MockKAnnotations.init(this)
        context = composeRule.activity.applicationContext
        navController = TestNavHostController(context)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        composeRule.setContent {
            TestCheckMateNavHost(
                startDestination = Screen.SignIn.route,
                navController = navController,
                signInViewModel = SignInViewModel(checklistRepository, userRepository),
                forgotPasswordViewModel = ForgotPasswordViewModel(checklistRepository, userRepository),
                signUpViewModel = SignUpViewModel(userRepository)
            )
        }
    }

    @Test
    fun verify_navigate_from_signIn_to_signUp() {
        composeRule
            .onNodeWithText(context.getString(R.string.sign_in_prompt_to_sign_up), true)
            .performClick()
        composeRule
            .onNodeWithText(context.getString(R.string.sign_up))
            .assertIsDisplayed()
    }

    @Test
    fun verify_navigate_from_signUp_to_signIn() {
        composeRule
            .onNodeWithText(context.getString(R.string.sign_in_prompt_to_sign_up), true)
            .performClick()
        composeRule
            .onNodeWithText(context.getString(R.string.sign_up_prompt_to_sign_in), true)
            .performClick()
        composeRule
            .onNodeWithText(context.getString(R.string.sign_in))
            .assertIsDisplayed()
    }
}
