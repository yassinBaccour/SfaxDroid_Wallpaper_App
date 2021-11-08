package com.sami.wallpapers.ui

import android.app.WallpaperManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sami.wallpapers.R

@Composable
fun Detail(id: Int?) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id!!),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(50.dp, 5.dp, 50.dp, 5.dp),
            colors = ButtonDefaults.buttonColors(PrimaryDark),
            onClick = { setAsWallpaper(id, context) }) {
            Text(
                color = Color.White,
                text = stringResource(id = R.string.set_basic_wallpaper_click_text),
            )
        }
    }
}

fun setAsWallpaper(resourceName: Int, context: Context) {
    WallpaperManager
        .getInstance(context).setResource(resourceName)
}

@Preview
@Composable
fun DetailPreview() {
    Detail(R.drawable.img_00005)
}

