package com.example.catlistexample.data.repository

import com.example.catlistexample.data.resource.RemoteCatListSource
import com.example.catlistexample.datastore.CatDataStore
import com.example.catlistexample.model.CatDataResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CatListRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteCatListSource,
    private val localDataStore: CatDataStore
) : CatDataRepository {

    override suspend fun getCats(): Flow<List<CatDataResponseItem>> {
        return remoteDataSource.fetchCatData()
            .map { response ->
                val catList = response.body() ?: emptyList()
                catList.map { cat ->
                    cat.copy(isFavorite = localDataStore.isCatSaved(cat.id).first())
                }
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun toggleCatFavorite(cat: CatDataResponseItem) {
        val isFavorite = localDataStore.isCatSaved(cat.id).first()
        if (isFavorite) {
            localDataStore.removeSavedCat(cat.id)
        } else {
            localDataStore.setSavedCat(cat.id, cat.url)
        }
    }

    fun getFavoriteCats(): Flow<List<CatDataResponseItem>> {
        return localDataStore.getAllSavedCats()
            .map { savedMap ->
                savedMap.map { (id, url) ->
                    CatDataResponseItem(
                        id = id,
                        url = url,
                        breeds = emptyList(),
                        height = 0,
                        width = 0,
                        isFavorite = true
                    )
                }
            }
    }

}
