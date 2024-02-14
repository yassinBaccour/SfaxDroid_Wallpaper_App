package com.sfaxdroid.list.pixabay.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sfaxdroid.detail.ActionTypeEnum
import com.sfaxdroid.detail.utils.DetailUtils
import com.sfaxdroid.list.R

@Composable
fun WallpaperDetail(navController: NavController, url: String?) {
    WallpaperDetail(url = url, viewModel = hiltViewModel()) {
        navController.popBackStack()
    }
}

@Composable
internal fun WallpaperDetail(url: String?, viewModel: DetailViewModel, navBack: () -> Unit) {
    val context = LocalContext.current
    WallpaperDetail(url, navBack) {
        viewModel.setAsWallpaper(it, context)
    }
}

@Composable
internal fun WallpaperDetail(url: String?, navBack: () -> Unit, setAsWallpaper: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = url,
            contentDescription = null
        )
        TopNavBar(navBack)
        SetAsWallButton(url, setAsWallpaper)
    }
}

@Composable
private fun BoxScope.TopNavBar(navBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        IconButton(
            modifier = Modifier.padding(
                start = 8.dp,
                end = 14.dp
            ),
            onClick = {
                navBack.invoke()
            },
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                tint = Color.White,
                contentDescription = null
            )
        }
        Text(
            text = stringResource(R.string.wallpaper_source),
            modifier = Modifier
                .padding(6.dp),
            color = Color.White,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun BoxScope.SetAsWallButton(url: String?, setAsWallpaper: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(bottom = 60.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            shape = RoundedCornerShape(15),
            onClick = {
                url?.let {
                    setAsWallpaper(url)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = stringResource(R.string.set_as_wallpaper),
                fontSize = 18.sp,
                style = MaterialTheme.typography.button
            )
        }
    }
}