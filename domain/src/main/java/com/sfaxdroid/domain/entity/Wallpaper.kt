package com.sfaxdroid.domain.entity

data class Wallpaper(
    val label: String,
    val detailUrl: String,
    val previewUrl: String,
    val tag: List<String>,
    val source: String
)