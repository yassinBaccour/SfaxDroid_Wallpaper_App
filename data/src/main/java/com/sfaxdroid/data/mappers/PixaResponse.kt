package com.sfaxdroid.data.mappers

data class PixaResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<PixaItem>
) {
    companion object {
        val empty = PixaResponse(0, 0, listOf())
    }
}
