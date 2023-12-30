package com.oukschub.checkmate.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.screen.Checklists
import com.oukschub.checkmate.ui.screen.CreateChecklist
import com.oukschub.checkmate.ui.screen.Home
import com.oukschub.checkmate.ui.screen.Profile
import com.oukschub.checkmate.ui.screen.SignIn
import com.oukschub.checkmate.util.FirebaseUtil

@Composable
fun CheckMateApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBarItems = listOf(Screen.Checklists, Screen.Home, Screen.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomAppBar(actions = {
                for (screen in navBarItems) {
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = { navController.navigate(screen.route) },
                        icon = {
                            Icon(
                                imageVector = screen.icon!!,
                                contentDescription = stringResource(screen.resourceId)
                            )
                        },
                        label = { Text(text = stringResource(screen.resourceId)) }
                    )
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CreateChecklist.route) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.desc_create_checklist)
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (FirebaseUtil.isLoggedIn()) Screen.Home.route else Screen.SignIn.route,
            modifier = Modifier.padding(paddingValues)
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
}
