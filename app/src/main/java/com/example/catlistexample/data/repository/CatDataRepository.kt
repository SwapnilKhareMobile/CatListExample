package com.example.catlistexample.data.repository

import com.example.catlistexample.model.CatDataResponse
import com.example.catlistexample.model.CatDataResponseItem
import com.example.catlistexample.model.FavCatDataResponseItem
import kotlinx.coroutines.flow.Flow

interface CatDataRepository {
    suspend fun combinedData(): Flow<List<FavCatDataResponseItem>>
    suspend fun toggleFavoriteCats(catId:String,url:String,isFavorite:Boolean)
    suspend fun getFavoriteCats(): Flow<List<FavCatDataResponseItem>>
}