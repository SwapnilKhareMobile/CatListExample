package com.example.catlistexample.ui.screens.favcat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistexample.data.repository.CatListRepositoryImpl
import com.example.catlistexample.model.CatDataResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCatsViewModel @Inject constructor(
    private val catRepository: CatListRepositoryImpl
) : ViewModel() {

    private val _favoriteCatList = MutableStateFlow<List<CatDataResponseItem>>(emptyList())
    val favoriteCatList: StateFlow<List<CatDataResponseItem>> = _favoriteCatList

    init {
        fetchFavoriteCatList()
    }

    private fun fetchFavoriteCatList() {
        viewModelScope.launch {
            catRepository.getFavoriteCats()
                .collectLatest { favorites ->
                    _favoriteCatList.value = favorites
                }
        }
    }

}