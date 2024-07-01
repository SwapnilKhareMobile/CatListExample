package com.example.catlistexample.data.resource

import com.example.catlistexample.model.CatDataResponse
import com.example.catlistexample.model.CatDataResponseItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {
   suspend fun fetchCatData(): Flow<List<CatDataResponseItem>>
}