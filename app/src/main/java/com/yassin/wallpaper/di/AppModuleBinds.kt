package com.yassin.wallpaper.di

import com.sfaxdroid.base.Ads
import com.sfaxdroid.base.AppInitializer
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.PrivacyManager
import com.sfaxdroid.base.SfaxDroidRating
import com.sfaxdroid.data.entity.Logger
import com.yassin.wallpaper.appInitializers.AdsInitializer
import com.yassin.wallpaper.appInitializers.TimberInitializer
import com.yassin.wallpaper.utils.DeviceHandler
import com.yassin.wallpaper.utils.FileHandler
import com.yassin.wallpaper.utils.Rating
import com.yassin.wallpaper.utils.SfaxDroidAds
import com.yassin.wallpaper.utils.SfaxDroidLogger
import com.yassin.wallpaper.utils.SfaxDroidPrivacy
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun provideFileManager(fileHandler: FileHandler): FileManager

    @Singleton
    @Binds
    abstract fun provideLogger(sfaxDroidLogger: SfaxDroidLogger): Logger

    @Singleton
    @Binds
    abstract fun provideAds(sfaxDroidAds: SfaxDroidAds): Ads

    @Singleton
    @Binds
    abstract fun providePrivacy(sfaxDroidPrivacy: SfaxDroidPrivacy): PrivacyManager

    @Singleton
    @Binds
    abstract fun provideDeviceManager(deviceHandler: DeviceHandler): DeviceManager

    @Singleton
    @Binds
    abstract fun provideRating(rating: Rating): SfaxDroidRating

    @Binds
    @IntoSet
    abstract fun provideTimberInitializer(timberInitializer: TimberInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideAdsInitializer(adsInitializer: AdsInitializer): AppInitializer

}