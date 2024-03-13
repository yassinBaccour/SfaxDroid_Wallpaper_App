package com.sfaxdroid.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sfaxdroid.data.entity.AppName
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.ItemWrapperList
import com.sfaxdroid.data.mappers.TagView
import com.sfaxdroid.list.WallpaperViewState
import com.sfaxdroid.list.viewmodels.WallpapersViewModel

@Composable
fun WallpaperList(
    openDetail: (BaseWallpaperView, String, String, AppName) -> Unit,
) {
    WallpaperList(viewModel = hiltViewModel(), openDetail = openDetail)
}

@Composable
internal fun WallpaperList(
    viewModel: WallpapersViewModel,
    openDetail: (BaseWallpaperView, String, String, AppName) -> Unit
) {
    val state by viewModel.state.collectAsState(
        initial = WallpaperViewState()
    )

    WallpaperList(
        state,
        openDetail = openDetail,
        updateSelectedPosition = { viewModel.updateSelectedPosition(it) },
        getWallpaperByTag = { viewModel.getWallpaperByTag(it) })
}

@Composable
internal fun WallpaperList(
    state: WallpaperViewState,
    openDetail: (BaseWallpaperView, String, String, AppName) -> Unit,
    updateSelectedPosition: (Int) -> Unit,
    getWallpaperByTag: (TagView) -> Unit,
) {
    Column(Modifier.background(MaterialTheme.colors.primary)) {

        if (state.isTagVisible)
            InitTagList(
                modifier = Modifier
                    .height(50.dp),
                tagList = state.tagList,
                selectedItem = state.tagSelectedPosition
            ) { tagView, pos ->
                updateSelectedPosition(pos)
                getWallpaperByTag(tagView)
            }

        InitWallpaperList(state.itemsList) {
            openDetail(it, state.selectedLwpName, state.screenName, AppName.AccountOne)
        }
    }
}

@Composable
private fun InitWallpaperList(
    itemsList: List<ItemWrapperList>,
    openImageDetail: (BaseWallpaperView) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(itemsList.size) { message ->
            val obj = itemsList[message]
            GenerateItem(obj) {
                openImageDetail(it)
            }
        }
    }
}

@Composable
fun InitTagList(
    modifier: Modifier,
    tagList: List<TagView>,
    selectedItem: Int,
    onTagClick: (TagView, Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier
    ) {
        items(tagList.size) {
            val obj = tagList[it]
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selectedItem == it) MaterialTheme.colors.onSecondary else Color.White)
                    .clickable { onTagClick(obj, it) }
            ) {
                Text(
                    obj.name,
                    Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.subtitle1,
                )
            }
        }
    }
}