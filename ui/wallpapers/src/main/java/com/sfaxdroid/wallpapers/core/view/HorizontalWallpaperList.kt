package com.sfaxdroid.wallpapers.core.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sfaxdroid.wallpapers.R
import com.sfaxdroid.wallpapers.core.WallpaperUiModel

@Composable
internal fun HorizontalWallpaperList(
    title: String,
    wallpapers: List<WallpaperUiModel>,
    openDetail: (String) -> Unit,
    openTag: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SectionTitle(title = title, openTag = openTag)
        Spacer(Modifier.height(10.dp))
        LazyRow {
            itemsIndexed(wallpapers) { index, wallpaper ->
                WallpaperItem(
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                        .padding(start = if (index == 0) 8.dp else 0.dp, end = 5.dp),
                    wallpaper = wallpaper,
                    onClick = { openDetail.invoke(wallpaper.detailUrl) }
                )
            }
        }
        BetweenSectionSpacer()
    }
}

