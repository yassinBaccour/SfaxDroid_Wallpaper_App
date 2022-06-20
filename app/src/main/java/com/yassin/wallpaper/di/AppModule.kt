package com.yassin.wallpaper.di

import android.content.Context
import androidx.core.os.ConfigurationCompat
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.PreferencesManager
import com.yassin.wallpaper.BuildConfig
import com.sfaxdroid.data.entity.AppName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideBaseUrl(): String = BuildConfig.APP_KEY

    @Provides
    fun provideJsonVersion(): String = BuildConfig.JSON_VERSION

    @Provides
    @Named("domain-url")
    fun provideFullUrl(): String = provideBaseUrl() + provideJsonVersion() + "/"

    @Provides
    @Named("interstitial-key")
    fun provideAdsInterstitialKey(): String = BuildConfig.APP_INTERSTITIAL_KEY

    @Provides
    @Named("appLanguage")
    fun getIsArabic(@ApplicationContext context: Context): String {
        val local = ConfigurationCompat.getLocales(context.resources.configuration)[0]
        return local?.language ?: "fr"
    }

    @Provides
    @Named("app-name")
    fun provideAppName(): AppName {
        return if (BuildConfig.FLAVOR == "accountTwo")
            AppName.AccountTwo
        else
            AppName.AccountOne
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
            AppName.AccountTwo -> "liliagame"
            AppName.AccountOne -> "sfaxdroid"
        }

        return PreferencesManager(
            context,
            name.plus(Constants.PREFERENCES_NAME)
        )
    }


}
