package com.sfaxdroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
