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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sfaxdroid.mini.base.Utils

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colors = if (BuildConfig.FLAVOR == "flower") FlowerColor else TawakolColor,
                typography = Typography
            ) {
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
                    .background(FlowerColor.primary),
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
                        style = Typography.h6,
                        text = stringResource(id = R.string.set_wallpaper_click_text),
                    )
                }

                Button(modifier = Modifier.padding(10.dp),
                    onClick = { Utils.ratingApplication(context) }) {
                    Text(
                        style = Typography.h6,
                        text = stringResource(id = R.string.setting_rate_us),
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
