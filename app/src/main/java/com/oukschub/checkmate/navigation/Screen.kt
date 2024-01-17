package com.oukschub.checkmate.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.oukschub.checkmate.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int = -1,
    val icon: ImageVector? = null
) {
    data object Splash : Screen("splash")
    data object SignIn : Screen("sign_in")
    data object SignUp : Screen("sign_up")
    data object Checklists : Screen("checklists", R.string.checklists, Icons.Default.List)
    data object Home : Screen("home", R.string.home, Icons.Default.Home)
    data object Profile : Screen("profile", R.string.profile, Icons.Default.Person)
    data object CreateChecklist : Screen("create_checklist")
}
