package com.sfaxdroid.data

import androidx.room.Dao
import androidx.room.Query
import com.sfaxdroid.data.entity.SfxWallpaper

@Dao
interface WallpaperDao {

    @get:Query("SELECT * FROM SfxWallpaper")
    val getAll: List<SfxWallpaper>

}