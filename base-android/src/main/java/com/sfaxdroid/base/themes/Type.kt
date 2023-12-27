package com.sfaxdroid.base.themes

import androidx.compose.material.Typography
import androidx.compose.ui.unit.sp


val defaultTypography = Typography()
val appTypography = Typography(
    h1 = defaultTypography.h1.copy(fontSize = 30.sp),
    h2 = defaultTypography.h2.copy(fontSize = 20.sp),
    h3 = defaultTypography.h3.copy(fontSize = 18.sp),
    h4 = defaultTypography.h4.copy(fontSize = 16.sp),
    h5 = defaultTypography.h5.copy(fontSize = 14.sp),
    h6 = defaultTypography.h6.copy(fontSize = 12.sp),
    subtitle1 = defaultTypography.subtitle1,
    subtitle2 = defaultTypography.subtitle2,
    body1 = defaultTypography.body1.copy(),
    body2 = defaultTypography.body2.copy(),
    button = defaultTypography.button.copy(),
    caption = defaultTypography.caption,
    overline = defaultTypography.overline.copy()
)