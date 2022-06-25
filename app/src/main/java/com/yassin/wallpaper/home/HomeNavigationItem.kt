package com.yassin.wallpaper.home

import androidx.compose.ui.graphics.vector.ImageVector
import com.yassin.wallpaper.Screen

class HomeNavigationItem(
    val screen: Screen,
    val labelResId: Int,
    val contentDescriptionResId: Int,
    val iconImageVector: Int,
    val iconImageVectorSelected: Int
)