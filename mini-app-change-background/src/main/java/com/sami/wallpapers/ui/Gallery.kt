package com.sami.wallpapers.ui

import android.app.WallpaperManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sami.wallpapers.Constants
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Gallery() {
    val iterator = (1..98).iterator()
    val context = LocalContext.current
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 128.dp)
    ) {
        iterator.forEach {
            val drawableRes = context.resources.getIdentifier(
                Constants.RESOURCE_PREFIX + String.format("%05d", it, Locale.US), "drawable",
                context.packageName
            )
            item {
                Image(
                    modifier = Modifier.clickable {
                        //TODO navigate to detail Screen
                    },
                    painter = painterResource(drawableRes),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
    }

    fun setAsWallpaper(resourceName: Int) {
        WallpaperManager
            .getInstance(context).setResource(resourceName)
    }
}