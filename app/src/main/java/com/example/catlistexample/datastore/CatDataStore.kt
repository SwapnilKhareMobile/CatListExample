package com.example.catlistexample.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.catlistexample.util.CAT_FAV_PREFIX
import com.example.catlistexample.util.CAT_URL_PREFIX
import com.example.catlistexample.util.DATA_STORE_PREF
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_PREF)

class CatDataStore(private val context: Context) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    companion object {
        val FAVORITE_CATS_KEY = stringSetPreferencesKey("favorite_cats")
        private fun catUrlKey(id: String) = stringPreferencesKey(CAT_URL_PREFIX + id)
        private fun catFavoriteKey(id: String) = booleanPreferencesKey(CAT_FAV_PREFIX + id)

    }

    val favoriteCatsList: Flow<Set<String>> = dataStore.data.map { preferences ->
        preferences[FAVORITE_CATS_KEY] ?: emptySet()
    }

    suspend fun setSavedCatWithId(catId: String,url:String, isFavorite: Boolean) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_CATS_KEY] ?: emptySet()
            if (isFavorite) {
                preferences[FAVORITE_CATS_KEY] = currentFavorites + catId
            } else {
                preferences[FAVORITE_CATS_KEY] = currentFavorites - catId
            }
            preferences[catUrlKey(catId)] = url
            preferences[catFavoriteKey(catId)] = true
        }
    }

    suspend fun removeSavedCat(catId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_CATS_KEY] ?: emptySet()
            preferences[FAVORITE_CATS_KEY] = currentFavorites - catId
            preferences.remove(catUrlKey(catId))
            preferences.remove(catFavoriteKey(catId))
        }
    }
    fun isCatSaved(id: String): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[catFavoriteKey(id)] ?: false
        }
    }
    fun getAllSavedCats(): Flow<Map<String, String>> {
        return context.dataStore.data.map { preferences ->
            preferences.asMap().filterKeys { it.name.startsWith(CAT_URL_PREFIX) }
                .mapKeys { it.key.name.removePrefix(CAT_URL_PREFIX) }
                .mapValues { it.value as String }
        }
    }

}