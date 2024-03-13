package com.sfaxdroid.data

import android.content.Context
import androidx.room.Room
import com.sfaxdroid.data.repositories.WallpapersDataSource
import com.sfaxdroid.data.repositories.WsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class WallpaperDataSource {

    @Provides
    @Singleton
    fun provideAppRepository(dataSource: WallpapersDataSource): WsRepository {
        return dataSource
    }
}
