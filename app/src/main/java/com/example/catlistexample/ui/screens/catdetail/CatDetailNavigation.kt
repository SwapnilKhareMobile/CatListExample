package com.example.catlistexample.ui.screens.catdetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.catlistexample.ui.screens.favcat.CAT_FAV_ROUTE
import com.example.catlistexample.ui.screens.favcat.CatFavScreen
import com.example.catlistexample.util.LINKED_CAT_RESOURCE_ID

const val CAT_ID_ARG = "catId"
const val DETAIL_ROUTE_BASE = "interests_route"
const val DETAIL_ROUTE = "$DETAIL_ROUTE_BASE?$CAT_ID_ARG={$CAT_ID_ARG}"

fun NavController.navigateToCatDetail(catId: String? =null, navOptions: NavOptions? = null) {
    val route = if (catId != null) {
        "${DETAIL_ROUTE_BASE}?${CAT_ID_ARG}=$catId"
    } else {
        DETAIL_ROUTE_BASE
    }
    navigate(route, navOptions)
}

fun NavGraphBuilder.catDetailScreen() {
    composable(
        route = DETAIL_ROUTE,
        arguments = listOf(
            navArgument(CAT_ID_ARG) { type = NavType.StringType }
        )
    ) {
            CatDetailScreen()
    }
}