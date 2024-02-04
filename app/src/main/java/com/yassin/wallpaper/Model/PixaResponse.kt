package com.yassin.wallpaper.Model

data class PixaResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<PixaPicture>
)
