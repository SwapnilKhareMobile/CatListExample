package com.example.catlistexample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.catlistexample.ui.CatAppState
import com.example.catlistexample.ui.screens.catdetail.catDetailScreen
import com.example.catlistexample.ui.screens.catdetail.navigateToCatDetail
import com.example.catlistexample.ui.screens.catlist.CAT_LIST_ROUTE
import com.example.catlistexample.ui.screens.catlist.catListScreen
import com.example.catlistexample.ui.screens.favcat.favouriteScreen

@Composable
fun CatAppNavHost(
    appState: CatAppState,
    modifier: Modifier = Modifier,
    startDestination: String = CAT_LIST_ROUTE,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        catListScreen { navController.navigateToCatDetail(it) }
        favouriteScreen { }
        catDetailScreen()
    }
}