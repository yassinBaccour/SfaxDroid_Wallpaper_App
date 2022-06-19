package com.yassin.wallpaper.di

import com.sfaxdroid.data.DeviceNetworkHandler
import com.yassin.wallpaper.utils.NetworkHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

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
