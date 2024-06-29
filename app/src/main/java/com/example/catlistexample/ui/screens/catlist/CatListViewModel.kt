package com.example.catlistexample.ui.screens.catlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistexample.data.repository.CatListRepositoryImpl
import com.example.catlistexample.model.CatDataResponseItem
import com.example.catlistexample.util.LINKED_CAT_RESOURCE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatListViewModel @Inject constructor(private val catRepository: CatListRepositoryImpl,private val savedStateHandle: SavedStateHandle,):ViewModel() {
    var mCatList = MutableStateFlow<CatListUIState>(CatListUIState.None)
    val catList:StateFlow<CatListUIState> = mCatList

    init {
        fetchCatListData()
    }

    fun fetchCatListData(){
        viewModelScope.launch {
            catRepository.getCats()
                .onStart { mCatList.value = CatListUIState.Loading }
                .catch { mCatList.value = CatListUIState.Error }
                .collectLatest { mCatList.value = CatListUIState.Success(it) }
        }

    }
     fun onToggleCatData(cat: CatDataResponseItem) {
         viewModelScope.launch {
             catRepository.toggleCatFavorite(cat)
             (catList.value as? CatListUIState.Success)?.let { successState ->
                 val updatedList = successState.cats.map {
                     if (it.id == cat.id) it.copy(isFavorite = !it.isFavorite) else it
                 }
                 mCatList.value = CatListUIState.Success(updatedList)
             }

         }
    }
}