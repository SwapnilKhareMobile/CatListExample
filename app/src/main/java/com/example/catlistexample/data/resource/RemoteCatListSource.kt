package com.example.catlistexample.data.resource

import com.example.catlistexample.model.CatDataResponse
import com.example.catlistexample.model.CatDataResponseItem
import com.example.catlistexample.network.APIHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class RemoteCatListSource @Inject constructor(private val apiHelper: APIHelper):RemoteDataSource {

   override suspend fun fetchCatData(): Flow<Response<CatDataResponse>> = flow {
               emit(apiHelper.getCatData())
    }

}