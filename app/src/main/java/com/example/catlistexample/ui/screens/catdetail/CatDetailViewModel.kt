package com.example.catlistexample.ui.screens.catdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CatDetailViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    val selectedTopicId: StateFlow<String?> = savedStateHandle.getStateFlow(CAT_ID_ARG, null)

}