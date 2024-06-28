package com.example.catlistexample.ui.screens.catlist

import com.example.catlistexample.model.CatDataResponseItem

sealed class CatListUIState {
    data object None : CatListUIState()
    data object Loading : CatListUIState()
    data class Success(val cats: List<CatDataResponseItem>) : CatListUIState()
    data object Error : CatListUIState()

}