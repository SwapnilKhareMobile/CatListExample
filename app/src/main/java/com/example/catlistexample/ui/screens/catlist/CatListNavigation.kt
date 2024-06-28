package com.example.catlistexample.ui.screens.catlist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.catlistexample.util.LINKED_CAT_RESOURCE_ID

const val CAT_LIST_ROUTE = "cat_list_route/{$LINKED_CAT_RESOURCE_ID}"

fun NavController.navigateToCatList(navOptions: NavOptions) = navigate(CAT_LIST_ROUTE, navOptions)

fun NavGraphBuilder.catListScreen(onCatItemClick: (String) -> Unit,) {
    composable(
        route = CAT_LIST_ROUTE,
        arguments = listOf(
            navArgument(LINKED_CAT_RESOURCE_ID) { type = NavType.StringType },
        ),
    ) {
        CatListScreen(onCatItemClick)
    }

}