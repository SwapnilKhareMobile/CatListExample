package com.example.catlistexample.ui.screens.catdetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CatDetailScreen(viewModel: CatDetailViewModel = hiltViewModel()) {
    val stateResult = viewModel.selectedTopicId.collectAsStateWithLifecycle()
    Text(modifier = Modifier.padding(16.dp), text = stateResult?.value?:"")
}