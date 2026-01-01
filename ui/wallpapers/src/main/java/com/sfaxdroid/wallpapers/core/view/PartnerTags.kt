package com.sfaxdroid.wallpapers.core.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
    Column {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(15.dp))
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tags.forEach { tags ->
                OutlinedButton(
                    modifier = Modifier.height(30.dp),
                    onClick = { openTag.invoke(tags) },
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
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = tags.first,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        BetweenSectionSpacer()
    }
}