package com.example.catlistexample.ui.screens.favcat

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.catlistexample.ui.screens.catlist.CatItems
import com.example.catlistexample.ui.screens.catlist.CatListViewModel
import dagger.Component

@Composable
fun CatFavScreen(onItemClick:(String) -> Unit,
                 viewModel: FavoriteCatsViewModel = hiltViewModel()
){
    val item by viewModel.favoriteCatList.collectAsStateWithLifecycle()
    when{
        item.isNullOrEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No favorite cats")
            }
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item.forEach {
                    item {
                        CatItems(catDataResponseItem = it)
                    }

                }
            }
        }
    }


}
@Composable
fun CatItems(catDataResponseItem: CatDataResponseItem) {

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
        }
    }
}
