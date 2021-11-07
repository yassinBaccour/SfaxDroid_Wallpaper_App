package com.sami.wallpapers.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sami.wallpapers.Constants
import com.sami.wallpapers.R
import java.util.*

@Composable
fun Gallery() {
    val iterator = (1..50).iterator()
    val context = LocalContext.current
    LazyRow(modifier = Modifier.fillMaxHeight(), ) {
        iterator.forEach {
            val drawableRes = context.resources.getIdentifier(
                Constants.RESOURCE_PREFIX + String.format("%05d", it, Locale.US), "drawable",
                context.packageName
            )
            item {
                Image(
                    modifier = Modifier
                        .width(400.dp)
                        .fillMaxHeight(),
                    painter = painterResource(drawableRes),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}