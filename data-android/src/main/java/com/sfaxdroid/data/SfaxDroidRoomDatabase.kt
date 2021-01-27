package com.sfaxdroid.data

import androidx.room.Database

import androidx.room.RoomDatabase
import com.sfaxdroid.data.entity.SfxWallpaper

@Database(entities = [SfxWallpaper::class], version = 2)
abstract class SfaxDroidRoomDatabase : RoomDatabase() {
    abstract fun wallpaperDao(): WallpaperDao
}