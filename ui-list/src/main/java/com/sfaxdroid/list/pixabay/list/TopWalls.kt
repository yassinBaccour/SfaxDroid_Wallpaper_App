package com.sfaxdroid.list.pixabay.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.sfaxdroid.bases.encodeUrl
import com.sfaxdroid.data.entity.TopWall

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun TopWalls(topWallList: List<TopWall>, openWallpaper: (String) -> Unit) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        modifier = Modifier.height(
            LocalConfiguration
                .current
                .screenHeightDp
                .dp / 3.25f
        ),
        count = topWallList.size,
        state = pagerState,
        contentPadding = PaddingValues(end = 16.dp, start = 16.dp, top = 16.dp),
        itemSpacing = 8.dp
    ) { page: Int ->
        val pageOffset = pagerState.currentPage - page + pagerState.currentPageOffset
        TopWallItem(
            item = topWallList[page],
            modifier = Modifier.fillMaxSize(),
            pageOffset = pageOffset,
            openWallpaper = openWallpaper,
            )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun TopWallItem(item: TopWall, modifier: Modifier = Modifier, pageOffset: Float, openWallpaper: (String) -> Unit ) {
    Card(
        modifier = modifier
            .padding(top = 4.dp, bottom = 16.dp)
            .graphicsLayer {
                lerp(
                    start = 0.85f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }
                alpha = lerp(
                    start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
            }
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            openWallpaper(item.pixaItem.largeImageURL.encodeUrl())
        }
    ) {

            AsyncImage(
                model = item.pixaItem.webformatURL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.6f,
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.catchySentence,
                    color = Color.White,
                    fontSize = 35.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "On Pixabay.com",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(5.dp)
                )
            }
    }
}