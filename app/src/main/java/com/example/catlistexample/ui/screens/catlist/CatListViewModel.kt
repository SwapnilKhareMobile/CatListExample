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
    private var catList = MutableStateFlow<CatListUIState>(CatListUIState.None)
    val _catList:StateFlow<CatListUIState> = catList

    val deepLinkedCatResource = savedStateHandle.getStateFlow<String?>("","")

    fun updateCatSelection(catId: String, isChecked: Boolean) {
        viewModelScope.launch {
        }
    }

    fun onCatItemClicked(catResourceId: String) {
        if (catResourceId == deepLinkedCatResource.value?:"") {
            savedStateHandle[LINKED_CAT_RESOURCE_ID] = null
        }
    }

    init {
        fetchCatListData()
    }

    private fun fetchCatListData(){
        viewModelScope.launch {
            catRepository.getCats()
                .onStart { catList.value = CatListUIState.Loading }
                .catch { catList.value = CatListUIState.Error }
                .collectLatest { catList.value = CatListUIState.Success(it) }
        }

    }
     fun onToggleCatData(cat: CatDataResponseItem) {
         viewModelScope.launch {
             catRepository.toggleCatFavorite(cat)
             (catList.value as? CatListUIState.Success)?.let { successState ->
                 val updatedList = successState.cats.map {
                     if (it.id == cat.id) it.copy(isFavorite = !it.isFavorite) else it
                 }
                 catList.value = CatListUIState.Success(updatedList)
             }

         }
    }
}