package com.sfaxdroid.data.di

import com.sfaxdroid.data.call.PartnerServer
import com.sfaxdroid.data.call.SfaxDroidServer
import com.sfaxdroid.data.call.createService
import com.sfaxdroid.data.service.PartnerApiService
import com.sfaxdroid.data.service.SfaxDroidServerApiService
import com.sfaxdroid.domain.config.AppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object WallpaperServiceModule {

    @Provides
    @Singleton
    internal fun providePartnerApiService(
        @SfaxDroidServer okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
        appConfig: AppConfig
    ) = okHttpClient.createService(
        baseUrl = appConfig.partnerBaseUrl,
        converter = converterFactory,
        service = PartnerApiService::class.java
    )

    @Provides
    @Singleton
    internal fun provideFmmApiService(
        @PartnerServer okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
        appConfig: AppConfig
    ) = okHttpClient.createService(
        baseUrl = appConfig.sfaxDroidBaseUrl,
        converter = converterFactory,
        service = SfaxDroidServerApiService::class.java
    )

}
