package com.oukschub.checkmate.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
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
import com.oukschub.checkmate.R

@Composable
fun CheckMateApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBarItems = listOf(Screen.Checklists, Screen.Home, Screen.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showFab = !navBackStackEntry.destinationEqualsTo(Screen.Profile.route)
    val showNavBar = navBackStackEntry.destinationEqualsTo(Screen.Checklists.route)
        || navBackStackEntry.destinationEqualsTo(Screen.Home.route)
        || navBackStackEntry.destinationEqualsTo(Screen.Profile.route)

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = showNavBar,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                BottomAppBar(actions = {
                    for (screen in navBarItems) {
                        NavigationBarItem(
                            selected = navBackStackEntry.destinationEqualsTo(screen.route),
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
            }
        },
        floatingActionButton = {
            if (showFab) {
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
    ) { paddingValues ->
        AppNavigation(
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}

private fun NavBackStackEntry?.destinationEqualsTo(destination: String): Boolean {
    if (this == null) {
        return false
    }

    return this.destination.hierarchy.any { it.route == destination }
}
