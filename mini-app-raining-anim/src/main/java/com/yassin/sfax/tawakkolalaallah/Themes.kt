package com.yassin.sfax.tawakkolalaallah

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val BlueColor = Color(41, 128, 185)
val GreenColor = Color(39, 174, 96)

val FlowerColor = darkColors(
    primary = BlueColor,
    onPrimary = BlueColor,
    primaryVariant = BlueColor,
    secondary = BlueColor,
    onSecondary = BlueColor,
    error = BlueColor,
    onError = BlueColor
)

val TawakolColor = darkColors(
    primary = GreenColor,
    onPrimary = GreenColor,
    primaryVariant = GreenColor,
    secondary = GreenColor,
    onSecondary = GreenColor,
    error = GreenColor,
    onError = GreenColor
)

val Typography = Typography(
    h6 = TextStyle(
        color = Color.White,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
)

@Composable
fun SfaxDroidThemes(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (BuildConfig.FLAVOR == "flower") FlowerColor else TawakolColor,
        typography = Typography
    ) {
        content()
    }
}