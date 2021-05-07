package com.sfaxdroid.data

import com.sfaxdroid.data.entity.SfxWallpaper
import javax.inject.Inject

class DbDataSource @Inject constructor(private val wallpaperDao: WallpaperDao) :
    DbRepository {

    override fun getAll(): List<SfxWallpaper> {
        return wallpaperDao.getAll()
    }

    override fun insertAll(wallpapers: List<SfxWallpaper>): List<Long> {
        return wallpaperDao.insertAll(wallpapers)
    }
}
