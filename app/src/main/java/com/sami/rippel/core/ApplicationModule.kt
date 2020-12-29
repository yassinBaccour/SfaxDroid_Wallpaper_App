package com.sami.rippel.core

import android.content.Context
import com.sami.rippel.WallpaperApplication
import com.sami.rippel.utils.AppName
import com.sami.rippel.utils.NetworkHandler
import com.sfaxdroid.base.Constants
import com.sfaxdroid.data.DeviceNetworkHandler
import com.sfaxdroid.data.repositories.Network
import com.sfaxdroid.data.repositories.WsRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule {


    @Provides
    fun provideContext(
        application: WallpaperApplication
    ): Context {
        return application
    }

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
    fun provideAppRepository(dataSource: Network): WsRepository {
        return dataSource
    }

    @Provides
    @Singleton
    fun provideRetrofit(@Named("domain-url") url: String): Retrofit {
        return Retrofit.Builder()
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()
    }

    @Provides
    @Singleton
    fun provideDeviceNetworkHandler(deviceNetworkHandler: NetworkHandler): DeviceNetworkHandler {
        return deviceNetworkHandler
    }

    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

}