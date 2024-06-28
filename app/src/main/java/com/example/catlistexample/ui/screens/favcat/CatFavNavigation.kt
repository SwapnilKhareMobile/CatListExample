package com.example.catlistexample.ui.screens.favcat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val CAT_FAV_ROUTE = "cat_fav_route"

fun NavController.navigateToFavourites(navOptions: NavOptions) = navigate(CAT_FAV_ROUTE, navOptions)

fun NavGraphBuilder.favouriteScreen(
    onCatItemClick: (String) -> Unit,
) {
    composable(route = CAT_FAV_ROUTE) {
        CatFavScreen(onCatItemClick)
    }
}
