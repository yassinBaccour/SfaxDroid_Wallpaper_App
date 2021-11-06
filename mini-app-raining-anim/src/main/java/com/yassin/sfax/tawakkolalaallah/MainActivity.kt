package com.yassin.sfax.tawakkolalaallah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sfaxdroid.mini.base.Utils

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colors = SfaxDroidColor) {
                MainScreen()
            }
        }
    }

    @Composable
    private fun MainScreen() {
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SfaxDroidColor.primary),
                painter = painterResource(R.drawable.wallpaper2),
                contentDescription = null,
                alignment = Alignment.TopCenter,
                contentScale = ContentScale.FillBounds
            )
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = { Utils.openLiveWallpaper<LiveWallpaper>(context) }) {
                    Text(
                        color = Color.White,
                        text = stringResource(id = R.string.set_wallpaper_click_text),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                }
                Button(modifier = Modifier.padding(10.dp),
                    onClick = { Utils.ratingApplication(context) }) {
                    Text(
                        color = Color.White,
                        text = stringResource(id = R.string.setting_rate_us),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                }
                Spacer(
                    Modifier
                        .height(24.dp)
                )
                val img = painterResource(id = R.drawable.ic_pub)
                Image(painter = img,
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
    }

    @Preview
    @Composable
    private fun Preview() {
        MainScreen()
    }

}
