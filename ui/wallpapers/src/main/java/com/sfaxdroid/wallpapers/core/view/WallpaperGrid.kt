package com.sfaxdroid.wallpapers.core.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sfaxdroid.wallpapers.core.WallpaperUiModel

@Composable
internal fun WallpaperGrid(
    wallpapers: List<WallpaperUiModel>,
    openDetail: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 4.dp),
        maxItemsInEachRow = 3,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        wallpapers.forEach { wallpaper ->
            WallpaperItem(
                Modifier.weight(1f),
                wallpaper = wallpaper,
                onClick = { openDetail.invoke(wallpaper.detailUrl) }
            )
        }
    }
}