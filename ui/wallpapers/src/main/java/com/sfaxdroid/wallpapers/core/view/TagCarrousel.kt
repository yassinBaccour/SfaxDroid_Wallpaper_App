package com.sfaxdroid.wallpapers.core.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun TagCarrousel(
    tags: List<Pair<String, String>>,
    openTag: (Pair<String, String>) -> Unit
) {
    LazyRow(Modifier.padding(vertical = 10.dp)) {
        items(tags) {
            OutlinedButton(
                modifier = Modifier.height(30.dp).padding(start = 4.dp),
                onClick = { openTag.invoke(it) },
                contentPadding = PaddingValues(horizontal = 1.dp, vertical = 1.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    modifier = Modifier.Companion.padding(horizontal = 5.dp),
                    text = it.first,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}