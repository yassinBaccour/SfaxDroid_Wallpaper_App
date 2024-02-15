package com.sfaxdroid.list.pixabay.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sfaxdroid.data.entity.PixaTagWithSearchData

@Composable
fun InitPixaTagList(
    modifier: Modifier,
    tagsWithSearchData: List<PixaTagWithSearchData>,
    selectedItem: Int,
    onTagClick: (PixaTagWithSearchData, Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier
    ) {
        items(tagsWithSearchData.size) {
            val obj = tagsWithSearchData[it]
            Box(
                modifier =
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedItem == it) MaterialTheme.colors.onSecondary
                        else Color.White
                    )
                    .clickable { onTagClick(obj, it) }
            ) {
                Text(
                    obj.tag.name,
                    Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.subtitle1,
                )
            }
        }
    }
}
