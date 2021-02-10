package com.sfaxdroid.data

import com.sfaxdroid.data.entity.SfxWallpaper

interface DbRepository {

    fun getAll(): List<SfxWallpaper>
}