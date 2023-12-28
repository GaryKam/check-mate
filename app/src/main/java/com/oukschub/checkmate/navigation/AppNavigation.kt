package com.oukschub.checkmate.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oukschub.checkmate.ui.screen.Checklists
import com.oukschub.checkmate.ui.screen.Home
import com.oukschub.checkmate.ui.screen.Profile
import com.oukschub.checkmate.viewmodel.HomeViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel()
) {
    val navController = rememberNavController()
    val items = listOf(Screen.Checklists, Screen.Home, Screen.Profile)

    Scaffold(
        bottomBar = {
            BottomAppBar(actions = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = { navController.navigate(screen.route) },
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = stringResource(screen.resourceId)
                            )
                        },
                        label = { Text(text = stringResource(screen.resourceId)) }
                    )
                }
            })
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Checklists.route) { Checklists() }
            composable(Screen.Home.route) { Home(homeViewModel) }
            composable(Screen.Profile.route) { Profile() }
        }
    }
}