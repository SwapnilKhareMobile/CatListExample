package com.example.catlistexample.ui.screens.catlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistexample.data.repository.CatDataRepository
import com.example.catlistexample.model.CatDataResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatListViewModel @Inject constructor(private val catRepository: CatDataRepository,):ViewModel() {
    var mCatList  = MutableStateFlow<CatListUIState>(CatListUIState.None)
    var catList:StateFlow<CatListUIState> = mCatList

    init {
        fetchCatListData()
    }

    fun fetchCatListData(){
        viewModelScope.launch {
            catRepository.combinedData()
                .map { saveableCatResources ->
                    CatListUIState.Success(saveableCatResources)
                }.catch { mCatList.value = CatListUIState.Error }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = CatListUIState.Loading
                )
                .collect{mCatList.value = it}

        }
    }
    fun onToggleCatData(cat: CatDataResponseItem) {
        viewModelScope.launch {
            val isFavorite = mCatList.value is CatListUIState.Success &&
                    (mCatList.value as CatListUIState.Success).cats.any { it.catItem.id == cat.id && it.isFavorite }
            catRepository.toggleFavoriteCats(cat.id,cat.url, !isFavorite)
        }
    }
}