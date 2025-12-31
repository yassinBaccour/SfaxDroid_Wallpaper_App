package com.sfaxdroid.data.dto

import androidx.annotation.Keep

@Keep
class SfaxDroidWallpaperDto(
    val id: String,
    val title: String,
    val path: String,
    val fileName: String,
    val nbImage: Int
)
