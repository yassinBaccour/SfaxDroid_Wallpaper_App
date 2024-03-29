package com.sami.wallpapers.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sami.wallpapers.*
import com.sami.wallpapers.R
import com.sfaxdroid.mini.base.Utils

@Composable
fun HomeScreen(
    checkedViewModel: CheckedViewModel,
    rateUs: () -> Unit,
    onSpeedClick: (speed: String) -> Unit,
    onQualityClick: (speed: String) -> Unit,
    navHostController: NavHostController? = null
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val selectedSpeed = remember { mutableStateOf("") }
    val selectedQuality = remember { mutableStateOf("") }

    val state by checkedViewModel.counterLiveDate.observeAsState(WallpaperPref("de", "ze")) // 3.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(GrayThemeColor.primary)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            painter = painterResource(R.drawable.ic_title),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.height(20.dp))
        AppButton(R.string.set_wallpaper_click_text) {
            Utils.openLiveWallpaper<WallpaperAppService>(
                context
            )
        }
        AppButton(R.string.open_gallery) {
            navHostController?.navigate("gallery_screen")
        }
        AppButton(R.string.setting_rate_us) {
            rateUs()
        }
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 10.dp),
            text = stringResource(id = R.string.setting_change_speed),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        AppRow(
            checkedViewModel,
            R.string.setting_speed_very_slow,
            Constants.PREF_VALUE_SPEED_1,
            state.speed
        ) { speed -> checkedViewModel.setSpeed(speed) }
        AppRow(
            checkedViewModel,
            R.string.setting_speed_slow,
            Constants.PREF_VALUE_SPEED_2,
            state.speed
        ) { speed -> checkedViewModel.setSpeed(speed) }
        AppRow(
            checkedViewModel,
            R.string.setting_speed_standard,
            Constants.PREF_VALUE_SPEED_3,
            state.speed
        ) { speed -> checkedViewModel.setSpeed(speed) }
        AppRow(
            checkedViewModel,
            R.string.setting_speed_very_fast,
            Constants.PREF_VALUE_SPEED_4,
            state.speed
        ) { speed -> checkedViewModel.setSpeed(speed) }
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 10.dp),
            text = stringResource(id = R.string.setting_image_quality),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        AppRow(
            checkedViewModel,
            R.string.setting_quality_low,
            Constants.PREF_VALUE_QUALITY_1,
            state.quality
        ) { speed -> checkedViewModel.setQuality(speed) }
        AppRow(
            checkedViewModel,
            R.string.setting_quality_hight,
            Constants.PREF_VALUE_QUALITY_2,
            state.quality
        ) { speed -> checkedViewModel.setQuality(speed) }
        Spacer(modifier = Modifier.height(20.dp))
        AppButton(R.string.setting_privacy) {
            privacy(context)
        }
    }
}

private fun privacy(context: Context) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(Constants.PRIVACY)
    )
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

@Composable
fun AppButton(textId: Int, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp, 5.dp, 30.dp, 5.dp),
        colors = ButtonDefaults.buttonColors(PrimaryDark),
        onClick = { onClick() }) {
        Text(
            color = Color.White,
            text = stringResource(id = textId),
        )
    }
}

@Composable
fun AppRow(
    checkedViewModel: CheckedViewModel,
    title: Int,
    value: String,
    selectedSpeed: String,
    onClick: (speed: String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(33.dp, 10.dp, 33.dp, 0.dp)
    ) {
        RadioButton(
            selected = selectedSpeed == value,
            onClick = { onClick(value) }
        )
        Text(
            text = stringResource(id = title),
            color = Color.White,
            modifier = Modifier.clickable {
                onClick(value)
            })
    }
}

@Preview
@Composable
private fun Preview() {
    SfaxDroidThemes {
        HomeScreen(CheckedViewModel(), ::rateUs, ::onSpeedClick, ::onSpeedClick)
    }
}

fun rateUs() {
}

fun onSpeedClick(speed: String) {
}