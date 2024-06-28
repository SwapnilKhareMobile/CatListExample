package com.example.catlistexample.di

import com.example.catlistexample.network.APIHelper
import com.example.catlistexample.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit():APIHelper{
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(networkLoggingInterceptor())
        addHeadersInterceptor(okHttpClient)
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .baseUrl(BASE_URL)
            .build()
            .create(APIHelper::class.java)
    }
    private fun networkLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return loggingInterceptor
    }

    private fun addHeadersInterceptor(httpClient: OkHttpClient.Builder){
        httpClient.addInterceptor{chain ->
            val original: Request = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .header(
                    "x-api-key",
                    "live_VcvNYvwdCt8JhGR9XkCIkAzoAb56tZGY0rK07uX6hsTlt1erYFi6MJ2M75HHeLdH"
                )
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }
    }
}