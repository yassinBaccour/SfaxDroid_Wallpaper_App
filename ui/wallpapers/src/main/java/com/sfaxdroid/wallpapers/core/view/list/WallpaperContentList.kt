package com.sfaxdroid.wallpapers.core.view.list

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
import com.sfaxdroid.wallpapers.core.view.BetweenSectionSpacer
import com.sfaxdroid.wallpapers.core.view.HorizontalWallpaperList
import com.sfaxdroid.wallpapers.core.view.PartnerTags
import com.sfaxdroid.wallpapers.core.view.SectionTitle
import com.sfaxdroid.wallpapers.core.view.WallpaperGrid
import com.sfaxdroid.wallpapers.core.view.WallpaperTags

@Composable
internal fun WallpaperContentList(
    modifier: Modifier = Modifier,
    group: List<GroupUiModel>,
    openDetail: (String) -> Unit,
    openTag: (String, Pair<String, String>, Boolean) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(group) { item ->
            when (item) {
                is GroupUiModel.HOME_HEADER -> Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 20.dp),
                    text = "Designer Islamic Wallpaper",
                    style = MaterialTheme.typography.headlineSmall
                )

                is GroupUiModel.CARROUSEL -> {
                    val title = stringResource(R.string.editor_choice)
                    HorizontalWallpaperList(
                        title = title,
                        wallpapers = item.list,
                        openDetail = openDetail
                    ) { openTag.invoke(title, Pair(item.showMoreTag, "Arabic"), false) }
                }

                is GroupUiModel.GRID -> WallpaperGrid(
                    wallpapers = item.list,
                    openDetail = openDetail
                )

                is GroupUiModel.TAG -> WallpaperTags(
                    title = stringResource(R.string.popular_tags),
                    tags = item.list,
                    openTag = { openTag.invoke(it.first, it, false) }
                )

                is GroupUiModel.RANDOM_GRID -> {
                    val title = stringResource(R.string.random_wallpaper)
                    GridSection(
                        title = title,
                        wallpapers = item.list,
                        openDetail = openDetail
                    ) {
                        openTag.invoke(title, Pair(item.showMoreTag, "Arabic"), false)
                    }
                }
                is GroupUiModel.OF_THE_DAY -> {
                    val title = stringResource(R.string.wallpaper_of_the_day)
                    GridSection(
                        title = title,
                        wallpapers = item.list,
                        openDetail = openDetail
                    ) { openTag.invoke(title, Pair(item.showMoreTag, "Arabic"), false) }
                }

                is GroupUiModel.PARTNER_TAG -> {
                    val title = stringResource(R.string.partner_tags)
                    PartnerTags(
                        title = title,
                        tags = item.list,
                        openTag = { openTag.invoke(it.first, it, true) }
                    )
                }
            }
        }
    }
}

@Composable
internal fun GridSection(
    title: String,
    wallpapers: List<WallpaperUiModel>,
    openDetail: (String) -> Unit,
    openTag: () -> Unit
) {
    Column {
        SectionTitle(title = title, openTag = openTag)
        Spacer(Modifier.height(15.dp))
        WallpaperGrid(wallpapers, openDetail)
        BetweenSectionSpacer()
    }
}