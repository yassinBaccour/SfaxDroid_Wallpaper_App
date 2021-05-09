package com.yassin.wallpaper.core.di

import android.content.Context
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.PreferencesManager
import com.yassin.wallpaper.BuildConfig
import com.yassin.wallpaper.utils.AppName
import com.yassin.wallpaper.utils.DeviceHandler
import com.yassin.wallpaper.utils.FileHandler
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
    fun provideBaseUrl(): String = BuildConfig.APP_KEY

    @Provides
    fun provideJsonVersion(): String = BuildConfig.JSON_VERSION

    @Provides
    @Named("domain-url")
    fun provideFullUrl(): String = provideBaseUrl() + provideJsonVersion() + "/"

    @Provides
    @Named("intertitial-key")
    fun provideAdsIntertiatailKey(): String = BuildConfig.APP_INTERTITIAL_KEY

    @Provides
    @Named("app-name")
    fun provideAppName(): AppName {
        return if (BuildConfig.FLAVOR == "scaryallwhotouch")
            AppName.LiliaGame
        else
            AppName.SfaxDroid
    }

    @Provides
    @Named("app-id")
    fun provideAppId(): String {
        return BuildConfig.APPLICATION_ID
    }

    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context,
        @Named("app-name") appName: AppName
    ): PreferencesManager {

        val name = when (appName) {
            AppName.LiliaGame -> "liliagame"
            AppName.SfaxDroid -> "sfaxdroid"
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
