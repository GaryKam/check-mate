package com.oukschub.checkmate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oukschub.checkmate.navigation.CheckMateNavHost
import com.oukschub.checkmate.navigation.Screen

@Composable
fun CheckMateApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = modifier,
        bottomBar = { NavigationBar(navController) },
        floatingActionButton = { CreateChecklistFab(navController) }
    ) { paddingValues ->
        CheckMateNavHost(
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}

@Composable
private fun NavigationBar(navController: NavHostController) {
    val navBarItems = listOf(Screen.Checklists, Screen.Home, Screen.Profile)
    val navBackStack by navController.currentBackStackEntryAsState()
    val showNavBar = navBackStack.destinationEqualsTo(Screen.Checklists.route) ||
        navBackStack.destinationEqualsTo(Screen.Home.route) ||
        navBackStack.destinationEqualsTo(Screen.Profile.route)

    AnimatedVisibility(
        visible = showNavBar,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        BottomAppBar(actions = {
            for (screen in navBarItems) {
                NavigationBarItem(
                    selected = navBackStack.destinationEqualsTo(screen.route),
                    onClick = {
                        if (!navBackStack.destinationEqualsTo(screen.route)) {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                popUpTo(Screen.Home.route)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = screen.icon!!,
                            contentDescription = stringResource(screen.resourceId)
                        )
                    },
                    label = { Text(stringResource(screen.resourceId)) }
                )
            }
        })
    }
}

@Composable
private fun CreateChecklistFab(navController: NavHostController) {
    val navBackStack by navController.currentBackStackEntryAsState()
    val showFab = navBackStack.destinationEqualsTo(Screen.Checklists.route) ||
        navBackStack.destinationEqualsTo(Screen.Home.route)

    AnimatedVisibility(
        visible = showFab,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(Screen.CreateChecklist.route) }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.desc_create_checklist)
            )
        }
    }
}

private fun NavBackStackEntry?.destinationEqualsTo(destination: String): Boolean {
    return this?.destination?.hierarchy?.any { it.route == destination } ?: false
}
