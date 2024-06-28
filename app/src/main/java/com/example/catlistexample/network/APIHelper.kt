package com.example.catlistexample.network

import com.example.catlistexample.model.CatDataResponse
import com.example.catlistexample.util.CAT_METHOD
import retrofit2.Response
import retrofit2.http.GET

interface APIHelper {

    @GET(CAT_METHOD)
    suspend fun getCatData():Response<CatDataResponse>
}