package com.sfaxdroid.wallpaper.di

import com.sfaxdroid.data.call.PartnerServer
import com.sfaxdroid.data.call.SfaxDroidServer
import com.sfaxdroid.domain.config.AppConfig
import com.sfaxdroid.wallpaper.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideAppConfig() = AppConfig(
        partnerBaseUrl = BuildConfig.PARTNERS_BASE_URL,
        partnerApiKey = BuildConfig.PARTNERS_API_KEY,
        sfaxDroidBaseUrl = BuildConfig.SFAXDROID_BASE_URL,
        jsonVersion = BuildConfig.JSON_V
    )

    @Singleton
    @Provides
    @SfaxDroidServer
    fun providePartnerOkHttpClient(
        httpClient: OkHttpClient
    ) = httpClient.newBuilder().build()

    @Singleton
    @Provides
    @PartnerServer
    fun provideFmmOkHttpClient(
        httpClient: OkHttpClient
    ) = httpClient.newBuilder().build()

    @Singleton
    @Provides
    fun provideDefaultOkHttpClient(): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        return httpClientBuilder.build()
    }

}