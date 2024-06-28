package com.example.catlistexample.data.resource

import com.example.catlistexample.model.CatDataResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {
   suspend fun fetchCatData(): Flow<Response<CatDataResponse>>
}