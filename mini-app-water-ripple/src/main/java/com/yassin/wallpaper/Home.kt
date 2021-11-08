package com.yassin.wallpaper

import android.app.WallpaperManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sfaxdroid.engine.Constants
import com.sfaxdroid.mini.base.Utils
import java.io.IOException

@Composable
fun Main(changeThemes: (theme: String) -> Unit) {
    val context = LocalContext.current
    val selectedTheme = remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_home))
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        if (BuildConfig.FLAVOR != "green") {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                alignment = Alignment.TopCenter,
                contentScale = ContentScale.Inside
            )
        }
        if (BuildConfig.FLAVOR != "pink") {
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = stringResource(id = R.string.choose_picture))
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier
                    .fillMaxWidth()
                    .padding(33.dp, 10.dp, 33.dp, 0.dp)
            ) {
                AppRow(
                    imgDrawable = R.drawable.ic_mokup1,
                    value = Constants.CHANGE_IMAGE_VALUE_ONE,
                    selectedSpeed = selectedTheme,
                    onClick = changeThemes
                )
                AppRow(
                    imgDrawable = R.drawable.ic_mokup2,
                    value = Constants.CHANGE_IMAGE_VALUE_TWO,
                    selectedSpeed = selectedTheme,
                    onClick = changeThemes
                )
                AppRow(
                    imgDrawable = R.drawable.ic_mokup3,
                    value = Constants.CHANGE_IMAGE_VALUE_THREE,
                    selectedSpeed = selectedTheme,
                    onClick = changeThemes
                )
            }
        }
        Button(
            modifier = Modifier
                .padding(50.dp, 50.dp, 50.dp, 0.dp)
                .fillMaxWidth(),
            onClick = {
                val myWallpaperManager = WallpaperManager
                    .getInstance(context)
                try {
                    myWallpaperManager.clear()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                Utils.openLiveWallpaper<WallpaperEngine>(context)
            }) {
            Text(
                color = Color.White,
                text = stringResource(id = R.string.set_wallpaper_click_text),
            )
        }
        Spacer(modifier = Modifier.weight(1F))
        Image(painter = painterResource(id = R.drawable.ic_pub),
            contentDescription = "",
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
                .clickable(
                    enabled = true,
                    onClickLabel = "Clickable image",
                    onClick = {
                        Utils.openPub(context)
                    }
                ))
    }
}

@Composable
fun AppRow(
    imgDrawable: Int,
    value: String,
    selectedSpeed: MutableState<String>,
    onClick: (theme: String) -> Unit
) {
    RadioButton(
        selected = selectedSpeed.value == value,
        onClick = {
            onClick(value)
            selectedSpeed.value = value
        })
    Image(
        modifier = Modifier.clickable {
            onClick(value)
            selectedSpeed.value = value
        },
        painter = painterResource(imgDrawable),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
    )
}