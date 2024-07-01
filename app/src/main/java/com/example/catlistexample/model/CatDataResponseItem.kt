package com.example.catlistexample.model

data class CatDataResponseItem(
    val breeds: List<Breed>,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int,
)