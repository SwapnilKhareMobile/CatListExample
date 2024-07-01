package com.example.catlistexample.data.repository

import com.example.catlistexample.data.resource.LocalCatDataSourceImpl
import com.example.catlistexample.data.resource.RemoteDataSource
import com.example.catlistexample.model.FavCatDataResponseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CatListRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataStore: LocalCatDataSourceImpl
) : CatDataRepository {
    override suspend fun combinedData(): Flow<List<FavCatDataResponseItem>> = combine(
        remoteDataSource.fetchCatData().map { response ->
            response
        },
        localDataStore.favoriteStream
    ) { catResources, favoriteIds ->
        catResources.map { catResource ->
            FavCatDataResponseItem(
                catItem = catResource,
                isFavorite = favoriteIds.contains(catResource.id)
            )
        }
    }

    override suspend fun toggleFavoriteCats(catId:String,url:String,isFavorite:Boolean) {
        localDataStore.toggleFavoriteCats(catId,url,isFavorite)
    }

    override suspend fun getFavoriteCats(): Flow<List<FavCatDataResponseItem>> {
       return localDataStore.getFavoriteCats()
    }

}
