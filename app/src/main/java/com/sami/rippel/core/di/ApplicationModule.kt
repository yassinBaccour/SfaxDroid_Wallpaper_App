package com.sami.rippel.core.di

import android.content.Context
import com.sami.rippel.WallpaperApplication
import com.sami.rippel.utils.AppName
import com.sami.rippel.utils.NetworkHandler
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.PreferencesManager
import com.sfaxdroid.data.DeviceNetworkHandler
import com.sfaxdroid.data.repositories.Network
import com.sfaxdroid.data.repositories.WsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @Provides
    @Named("domain-url")
    fun provideBaseUrl(): String = "http://androidsporttv.com/yassin123441/"

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
            AppName.LILIAGAME -> "f24"
            AppName.SFAXDROID -> "rfi"
        }

        return PreferencesManager(
            context,
            name.plus(Constants.PREFERENCES_NAME)
        )
    }



}