package com.sfaxdroid.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.CategoryItem
import com.sfaxdroid.data.mappers.ItemWrapperList
import com.sfaxdroid.data.mappers.LwpItem
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.list.CarouselView
import com.sfaxdroid.list.adapter.WallpapersListAdapter

@Composable
fun GenerateItem(obj: ItemWrapperList, openImageDetail: (BaseWallpaperView) -> Unit) {
    when (obj.itemType) {
        WallpapersListAdapter.TYPE_WALLPAPER -> {
            val item = obj.`object` as SimpleWallpaperView
            ImageCard(item.thumbnailUrl) {
                openImageDetail(item)
            }
        }
        WallpapersListAdapter.TYPE_CAROUSEL -> {
            val item = obj.`object` as CarouselView
            HorizontalCarouselCard(item) {
            }
        }

        WallpapersListAdapter.TYPE_LWP -> {
            val item = obj.`object` as LwpItem
            LiveWallpaperCard(item) {
                openImageDetail(item)
            }
        }

        WallpapersListAdapter.TYPE_CAT -> {
            val item = obj.`object` as CategoryItem
            CategoryCard(item) {
                openImageDetail(item)
            }
        }
    }
}

@Composable
fun ImageCard(url: String, openImage: () -> Unit) {
    AsyncImage(
        model = url,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(128.dp)
            .clickable {
                openImage()
            }
    )
}

@Composable
fun LiveWallpaperCard(lwp: LwpItem, openImage: () -> Unit) {

}

@Composable
fun CategoryCard(categoryItem: CategoryItem, openImage: () -> Unit) {

}

@Composable
fun HorizontalCarouselCard(carouselView: CarouselView, openImage: () -> Unit) {

}