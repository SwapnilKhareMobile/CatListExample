package com.example.catlistexample.data.resource

import com.example.catlistexample.model.CatDataResponseItem
import com.example.catlistexample.network.APIHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteCatDataSourceImpl @Inject constructor(private val apiHelper: APIHelper):RemoteDataSource {

   override suspend fun fetchCatData(): Flow<List<CatDataResponseItem>> = flow {
               emit(apiHelper.getCatData().body()?.toList()?: emptyList())
    }.flowOn(Dispatchers.IO)

}