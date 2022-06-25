package com.sfaxdroid.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.CategoryItem
import com.sfaxdroid.data.mappers.ItemWrapperList
import com.sfaxdroid.data.mappers.LwpItem
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.list.CarouselView
import com.sfaxdroid.list.ListUtils.TYPE_CAROUSEL
import com.sfaxdroid.list.ListUtils.TYPE_CAT
import com.sfaxdroid.list.ListUtils.TYPE_LWP
import com.sfaxdroid.list.ListUtils.TYPE_WALLPAPER

@Composable
fun GenerateItem(obj: ItemWrapperList, openImageDetail: (BaseWallpaperView) -> Unit) {
    when (obj.itemType) {

         TYPE_WALLPAPER -> {
            val item = obj.`object` as SimpleWallpaperView
            ImageCard(item.thumbnailUrl) {
                openImageDetail(item)
            }
        }

         TYPE_CAROUSEL -> {
            val item = obj.`object` as CarouselView
            HorizontalCarouselCard(item) {
            }
        }

        TYPE_LWP -> {
            val item = obj.`object` as LwpItem
            ImageWithTitleCard(item.thumbnailUrl, item.name, item.desc) {
                openImageDetail(item)
            }
        }

        TYPE_CAT -> {
            val item = obj.`object` as CategoryItem
            ImageWithTitleCard(item.thumbnailUrl, item.name, item.desc) {
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
fun ImageWithTitleCard(url: String, name: String, desc: String, openImage: () -> Unit) {
    Card(
        Modifier
            .padding(top = 5.dp)
            .clickable {
                openImage()
            }
            .fillMaxWidth()
            .height(170.dp), elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
        ) {
            AsyncImage(
                model = url,
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            )
            Column {
                Text(
                    text = name,
                    modifier = Modifier.padding(16.dp, 24.dp, 16.dp, 12.dp),
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    desc,
                    Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp),
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.subtitle2
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
    ImageWithTitleCard("", "", "") {
    }
}