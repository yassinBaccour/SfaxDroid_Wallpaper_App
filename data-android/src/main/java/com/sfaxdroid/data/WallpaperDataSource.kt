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

    @Singleton
    @Provides
    fun providesRoomDatabase(context: Context): SfaxDroidRoomDatabase {
        return Room.databaseBuilder(
            context,
            SfaxDroidRoomDatabase::class.java,
            "SfaxDroid"
        ).build()
    }

    @Singleton
    @Provides
    fun providesWallpaperDao(database: SfaxDroidRoomDatabase): WallpaperDao {
        return database.wallpaperDao()
    }

    @Singleton
    @Provides
    fun providesDbDataSource(productDao: WallpaperDao): DbDataSource {
        return DbDataSource(productDao)
    }
}
