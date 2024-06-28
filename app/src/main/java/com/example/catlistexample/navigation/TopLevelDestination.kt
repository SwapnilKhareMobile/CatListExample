package com.example.catlistexample.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.catlistexample.R

enum class TopLevelDestination(
    val icon: ImageVector,
    val iconTextId: Int,
) {
    CAT_LIST(
        icon = Icons.Outlined.Home,
        iconTextId = R.string.cat_list
    ),

    CAT_FAVORITES(
       icon = Icons.Outlined.FavoriteBorder,
        iconTextId = R.string.favourite_cat
    )

}