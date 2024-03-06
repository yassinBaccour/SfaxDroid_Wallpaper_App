package com.sfaxdroid.list.pixabay.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sfaxdroid.data.entity.PixaTagWithSearchData

@Composable
fun MixedScreen(
    tagsWithSearchData: List<PixaTagWithSearchData>,
    onTagClick: (PixaTagWithSearchData, Int) -> Unit
) {
    LazyColumn {
        items(tagsWithSearchData.size) { index ->
            MixedItem(tagsWithSearchData[index], index + 1, onTagClick)
        }
    }
}

@Composable
fun MixedItem(
    tagWithSearchData: PixaTagWithSearchData,
    index: Int,
    onTagClick: (PixaTagWithSearchData, Int) -> Unit
) {
    Box(
        modifier =
        Modifier.fillMaxWidth().height(220.dp).background(Color.Black).clickable {
            onTagClick(tagWithSearchData, index)
            Log.d("MixedScreen", "MixedScreen: $index")
        }
    ) {
        AsyncImage(
            model = tagWithSearchData.tagImgUrl.collectAsState().value,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.8f,
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = tagWithSearchData.tag.name,
                color = Color.White,
                fontSize = 35.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(5.dp),
            )
            Text(
                text = "High Quality Wallpapers",
                color = Color.White,
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
