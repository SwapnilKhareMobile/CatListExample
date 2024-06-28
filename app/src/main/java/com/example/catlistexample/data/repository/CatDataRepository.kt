package com.example.catlistexample.data.repository

import com.example.catlistexample.model.CatDataResponse
import com.example.catlistexample.model.CatDataResponseItem
import kotlinx.coroutines.flow.Flow

interface CatDataRepository {
    suspend fun getCats() : Flow<List<CatDataResponseItem>>
    suspend fun toggleCatFavorite(cat: CatDataResponseItem)
}