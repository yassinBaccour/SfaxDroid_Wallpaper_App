package com.sfaxdroid.data

import com.sfaxdroid.data.repositories.WallpaperRepositoryImpl
import com.sfaxdroid.data.repositories.WallpaperRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class WallpaperModule {

    @Provides
    @Singleton
    fun provideAppRepository(dataSource: WallpaperRepositoryImpl): WallpaperRepository {
        return dataSource
    }
}
