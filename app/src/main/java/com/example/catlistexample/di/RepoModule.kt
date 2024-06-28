package com.example.catlistexample.di

import android.content.Context
import com.example.catlistexample.data.repository.CatDataRepository
import com.example.catlistexample.data.repository.CatListRepositoryImpl
import com.example.catlistexample.data.resource.RemoteCatListSource
import com.example.catlistexample.data.resource.RemoteDataSource
import com.example.catlistexample.datastore.CatDataStore
import com.example.catlistexample.network.APIHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {


    @Provides
    @Singleton
    fun provideRemoteDataSource(catApiService: APIHelper): RemoteDataSource {
        return RemoteCatListSource(catApiService)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): CatDataStore {
        return CatDataStore(context)
    }

    @Provides
    @Singleton
    fun provideCatRepository(
        remoteDataSource: RemoteCatListSource,
        bookmarkDataStore: CatDataStore
    ): CatDataRepository {
        return CatListRepositoryImpl(remoteDataSource, bookmarkDataStore)
    }

}