package com.sfaxdroid.data

import com.sfaxdroid.data.entity.SfxWallpaper

interface DbRepository {

    fun getAll(): List<SfxWallpaper>

    fun insertAll(wallpapers: List<SfxWallpaper>): List<Long>
}
