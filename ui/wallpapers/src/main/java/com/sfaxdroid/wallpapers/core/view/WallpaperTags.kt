package com.sfaxdroid.wallpapers.core.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun WallpaperTags(tags: List<String>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).padding(bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        tags.forEach { tags ->
            Button(
                onClick = {},
                contentPadding = PaddingValues(horizontal = 1.dp, vertical = 1.dp)
            ) {
                Text(
                    text = tags,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}