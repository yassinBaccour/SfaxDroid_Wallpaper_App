package com.sami.wallpapers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SplashScreen() {
    SfaxDroidThemes {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(GrayThemeColor.primary)
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Image(
                painter = painterResource(R.drawable.ic_title),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.height(50.dp))
            CircularProgressIndicator(color = GrayThemeColor.secondary)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SfaxDroidThemes {
        SplashScreen()
    }
}