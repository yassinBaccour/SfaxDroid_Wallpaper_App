package com.yassin.wallpaper.core.di

import com.sfaxdroid.base.AppInitializer
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.data.entity.Logger
import com.yassin.wallpaper.core.appInitializers.TimberInitializer
import com.yassin.wallpaper.utils.DeviceHandler
import com.yassin.wallpaper.utils.FileHandler
import com.yassin.wallpaper.utils.SfaxDroidLogger
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
    abstract fun provideDeviceManager(deviceHandler: DeviceHandler): DeviceManager

    @Binds
    @IntoSet
    abstract fun provideTimberInitializer(bind: TimberInitializer): AppInitializer
}