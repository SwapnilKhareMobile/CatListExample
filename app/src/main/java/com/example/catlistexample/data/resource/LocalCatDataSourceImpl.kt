package com.example.catlistexample.data.resource
import com.example.catlistexample.datastore.CatDataStore
import com.example.catlistexample.model.CatDataResponseItem
import com.example.catlistexample.model.FavCatDataResponseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalCatDataSourceImpl @Inject constructor(private val dataStore: CatDataStore) {

    val favoriteStream: Flow<List<String>> = dataStore.favoriteCatsList.map { favoriteSet ->
        favoriteSet.toList() // Convert the set to a list for easy manipulation
    }

    suspend fun toggleFavoriteCats(catId: String,url:String, isFavorite: Boolean) {
        if (isFavorite) {
            dataStore.setSavedCatWithId(catId, url,true)
        } else {
            dataStore.removeSavedCat(catId)
        }
    }
    fun getFavoriteCats(): Flow<List<FavCatDataResponseItem>> {
        return dataStore.getAllSavedCats()
            .map { savedMap ->
                savedMap.map { (id, url) ->
                    FavCatDataResponseItem(
                        CatDataResponseItem(
                            id = id,
                            url = url,
                            breeds = emptyList(),
                            height = 0,
                            width = 0
                        ),
                        isFavorite = true
                    )
                }
            }
    }
}