package com.sfaxdroid.wallpapers.core.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun PartnerTags(
    title: String,
    tags: List<Pair<String, String>>,
    openTag: (Pair<String, String>) -> Unit
) {
    if (tags.isNotEmpty()) {
        Column {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(15.dp))
            TagRows(tags, openTag)
            BetweenSectionSpacer()
        }
    }
}

