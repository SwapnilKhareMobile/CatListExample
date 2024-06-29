package com.example.catlistexample.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.catlistexample.util.CAT_FAV_PREFIX
import com.example.catlistexample.util.CAT_URL_PREFIX
import com.example.catlistexample.util.DATA_STORE_PREF
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(DATA_STORE_PREF)

class CatDataStore(private val context: Context) {

    companion object {
        private fun catUrlKey(id: String) = stringPreferencesKey(CAT_URL_PREFIX + id)
        private fun catFavoriteKey(id: String) = booleanPreferencesKey(CAT_FAV_PREFIX + id)
    }

    suspend fun setSavedCat(id: String, url: String) {
        context.dataStore.edit { preferences ->
            preferences[catUrlKey(id)] = url
            preferences[catFavoriteKey(id)] = true
        }
    }

    suspend fun removeSavedCat(id: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(catUrlKey(id))
            preferences.remove(catFavoriteKey(id))
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