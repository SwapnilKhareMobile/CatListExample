package com.example.catlistexample.model

data class FavCatDataResponseItem(
    val catItem:CatDataResponseItem,
    val isFavorite: Boolean = false
)