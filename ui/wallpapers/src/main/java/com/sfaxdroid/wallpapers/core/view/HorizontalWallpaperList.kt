package com.sfaxdroid.wallpapers.core.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sfaxdroid.wallpapers.core.WallpaperUiModel

@Composable
internal fun HorizontalWallpaperList(
    wallpapers: List<WallpaperUiModel>,
    openDetail: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        SectionTitle("Editor Choice"){}
        Spacer(Modifier.height(10.dp))
        LazyRow {
            items(wallpapers) {
                WallpaperItem(
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                        .padding(end = 5.dp),
                    wallpaper = it,
                    onClick = { openDetail.invoke(it.detailUrl) }
                )
            }
        }
        BetweenSectionSpacer()
    }
}

