package com.sfaxdroid.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sfaxdroid.data.entity.LiveWallpaper
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
    Card(
        Modifier
            .padding(top = 5.dp)
            .clickable {
                openImage()
            }
            .fillMaxWidth()
            .height(170.dp), elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
        ) {
            AsyncImage(
                model = lwp.thumbnailUrl,
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(100.dp)
            )
            Column {
                Text(text = lwp.name, modifier = Modifier.padding(16.dp, 24.dp, 16.dp, 12.dp))
                Text(text = lwp.desc, modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp))
            }

        }
    }
}

@Composable
fun CategoryCard(categoryItem: CategoryItem, openImage: () -> Unit) {
    Card(
        Modifier
            .padding(top = 5.dp)
            .clickable {
                openImage()
            }
            .fillMaxWidth()
            .height(170.dp), elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
        ) {
            AsyncImage(
                model = categoryItem.thumbnailUrl,
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .background(Color(android.graphics.Color.parseColor(categoryItem.color)))
                    .width(100.dp)
                    .fillMaxHeight()
            )
            Column {
                Text(
                    text = categoryItem.name,
                    modifier = Modifier.padding(16.dp, 24.dp, 16.dp, 12.dp)
                )
                Text(
                    text = categoryItem.desc,
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp)
                )
            }

        }
    }
}

@Composable
fun HorizontalCarouselCard(carouselView: CarouselView, openImage: () -> Unit) {

}


@Preview
@Composable
fun LiveWallpaperCardPreview() {
    LiveWallpaperCard(LwpItem("name", "desc", LiveWallpaper.None, "url")) {}
}