package com.example.catlistexample.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.util.trace
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.catlistexample.ui.screens.favcat.CAT_FAV_ROUTE
import com.example.catlistexample.ui.screens.catlist.CAT_LIST_ROUTE
import com.example.catlistexample.navigation.TopLevelDestination
import com.example.catlistexample.ui.screens.catlist.navigateToCatList
import com.example.catlistexample.ui.screens.favcat.navigateToFavourites
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberCatAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): CatAppState {
    return remember(navController, coroutineScope) {
        CatAppState(navController = navController)
    }
}

@Stable
class CatAppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            when (topLevelDestination) {
                TopLevelDestination.CAT_LIST -> navController.navigateToCatList(topLevelNavOptions)
                TopLevelDestination.CAT_FAVORITES -> navController.navigateToFavourites(
                    topLevelNavOptions
                )
            }
        }
    }

}
