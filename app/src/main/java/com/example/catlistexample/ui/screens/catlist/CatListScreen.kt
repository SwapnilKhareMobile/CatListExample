package com.example.catlistexample.ui.screens.catlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.catlistexample.R
import com.example.catlistexample.model.CatDataResponseItem

@Composable
fun CatListScreen(
    onCatItemClick: (String) -> Unit,
    viewModel: CatListViewModel = hiltViewModel()
) {
    val state by viewModel.catList.collectAsStateWithLifecycle()
    when (state) {
        CatListUIState.Error -> ShowErrorMsg()

        CatListUIState.Loading -> ShowProgress()

        CatListUIState.None -> {}
        is CatListUIState.Success -> {
            val item = (state as CatListUIState.Success).cats
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item.forEach {
                    item {
                        CatItems(
                            catDataResponseItem = it,
                            onCatItemClick,
                            viewModel::onToggleCatData
                        )
                    }

                }
            }
        }
    }

}

@Composable
fun CatItems(
    catDataResponseItem: CatDataResponseItem, onCatItemClick: (String) -> Unit,
    onToggleClick: (CatDataResponseItem) -> Unit
) {
    val isFavorite = catDataResponseItem.isFavorite

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                when {
                    catDataResponseItem.breeds?.isNullOrEmpty() == true -> onCatItemClick(
                        catDataResponseItem.id
                    )

                    else -> onCatItemClick(
                        catDataResponseItem.breeds?.get(0)?.description ?: catDataResponseItem.id
                    )
                }
            }
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {

            Image(
                painter = rememberAsyncImagePainter(model = catDataResponseItem.url),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {

                Text(
                    text = catDataResponseItem.id,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Icon(
                painter = painterResource(
                    if (isFavorite) R.drawable.ic_select else R.drawable.ic_unselect
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onToggleClick(catDataResponseItem) }
            )
        }
    }
}

@Composable
fun ShowErrorMsg() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Please try again")
    }
}

@Composable
fun ShowProgress() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}