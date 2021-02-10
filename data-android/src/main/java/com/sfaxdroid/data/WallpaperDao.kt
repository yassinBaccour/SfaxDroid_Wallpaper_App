package com.sfaxdroid.data

import androidx.room.*
import com.sfaxdroid.data.entity.SfxWallpaper

@Dao
interface WallpaperDao {

    @Query("SELECT * FROM wallpaper_table")
    fun getAll(): List<SfxWallpaper>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(wallpapers: List<SfxWallpaper>): List<Long>

    @Update
    fun update(wallpaper: SfxWallpaper): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(wallpaper: SfxWallpaper): Long

}