package com.sfaxdroid.wallpapers.core.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sfaxdroid.wallpapers.R
import com.sfaxdroid.wallpapers.core.GroupUiModel
import com.sfaxdroid.wallpapers.core.WallpaperUiModel

@Composable
internal fun WallpaperContentList(
    modifier: Modifier = Modifier,
    group: List<GroupUiModel>,
    openDetail: (String) -> Unit,
    openTag: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(group) {
            when (it) {
                is GroupUiModel.HOME_HEADER -> Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).padding(bottom = 20.dp),
                    text = "Designer Islamic Wallpaper",
                    style = MaterialTheme.typography.headlineSmall
                )

                is GroupUiModel.CARROUSEL -> HorizontalWallpaperList(it.list, openDetail)
                is GroupUiModel.GRID -> WallpaperGrid(it.list, openDetail)
                is GroupUiModel.TAG -> WallpaperTags(
                    stringResource(R.string.popular_tags),
                    it.list,
                    openTag
                )

                is GroupUiModel.RANDOM_GRID -> GridSection(
                    stringResource(R.string.random_wallpaper),
                    it.list,
                    openDetail
                )

                is GroupUiModel.OF_THE_DAY -> GridSection(
                    stringResource(R.string.wallpaper_of_the_day),
                    it.list,
                    openDetail
                )
            }
        }
    }
}

@Composable
internal fun GridSection(
    title: String,
    wallpapers: List<WallpaperUiModel>,
    openDetail: (String) -> Unit
) {
    Column {
        SectionTitle(title = title) {}
        Spacer(Modifier.height(15.dp))
        WallpaperGrid(wallpapers, openDetail)
        BetweenSectionSpacer()
    }
}