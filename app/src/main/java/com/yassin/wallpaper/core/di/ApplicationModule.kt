package com.yassin.wallpaper.core.di

import android.content.Context
import com.yassin.wallpaper.BuildConfig
import com.yassin.wallpaper.utils.AppName
import com.yassin.wallpaper.utils.DeviceHandler
import com.yassin.wallpaper.utils.FileHandler
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @Provides
    @Named("domain-url")
    fun provideBaseUrl(): String = BuildConfig.APP_KEY

    @Provides
    @Named("app-name")
    fun provideAppName(): AppName {
        return AppName.SFAXDROID
    }

    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context,
        @Named("app-name") appName: AppName
    ): PreferencesManager {

        val name = when (appName) {
            AppName.LILIAGAME -> "liliagame"
            AppName.SFAXDROID -> "sfaxdroid"
        }

        return PreferencesManager(
            context,
            name.plus(Constants.PREFERENCES_NAME)
        )
    }

    @Provides
    @Singleton
    fun provideFileManager(fileHandler: FileHandler): FileManager {
        return fileHandler
    }

    @Provides
    @Singleton
    fun provideDeviceManager(deviceHandler: DeviceHandler): DeviceManager {
        return deviceHandler
    }
}
