package com.sami.wallpapers

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val GrayColor = Color(127, 140, 141)
val BlueDark = Color(44, 62, 80)

val GrayThemeColor = lightColors(
    primary = GrayColor,
    primaryVariant = GrayColor,
    secondary = BlueDark,
    secondaryVariant = BlueDark,
    onSecondary = BlueDark,
    error = GrayColor
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
        colors = GrayThemeColor,
        typography = Typography
    ) {
        content()
    }
}