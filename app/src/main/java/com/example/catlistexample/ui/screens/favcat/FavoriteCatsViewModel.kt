package com.example.catlistexample.ui.screens.favcat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistexample.data.repository.CatDataRepository
import com.example.catlistexample.data.repository.CatListRepositoryImpl
import com.example.catlistexample.model.FavCatDataResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCatsViewModel @Inject constructor(
    private val catRepository: CatDataRepository
) : ViewModel() {

    private val _favoriteCatList = MutableStateFlow<List<FavCatDataResponseItem>>(emptyList())
    val favoriteCatList: StateFlow<List<FavCatDataResponseItem>> = _favoriteCatList

    init {
        fetchFavoriteCatList()
    }

    fun fetchFavoriteCatList() {
        viewModelScope.launch {
            catRepository.getFavoriteCats()
                .collectLatest { favorites ->
                    _favoriteCatList.value = favorites
                }
        }
    }

}