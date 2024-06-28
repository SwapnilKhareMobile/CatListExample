package com.example.catlistexample.ui.screens.catdetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CatDetailScreen(viewModel: CatDetailViewModel = hiltViewModel()) {
    val stateResult = viewModel.selectedTopicId.collectAsState()
    Text(text = stateResult?.value?:"")
}