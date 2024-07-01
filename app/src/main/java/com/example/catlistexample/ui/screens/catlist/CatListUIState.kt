package com.example.catlistexample.ui.screens.catlist

import com.example.catlistexample.model.CatDataResponseItem
import com.example.catlistexample.model.FavCatDataResponseItem

sealed class CatListUIState {
    data object None : CatListUIState()
    data object Loading : CatListUIState()
    data class Success(val cats: List<FavCatDataResponseItem>) : CatListUIState()
    data object Error : CatListUIState()

}