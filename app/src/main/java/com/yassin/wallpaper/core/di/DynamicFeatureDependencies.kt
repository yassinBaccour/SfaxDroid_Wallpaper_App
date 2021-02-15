package com.yassin.wallpaper.core.di

import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DynamicFeatureDependencies {
    fun fileManager(): FileManager
    fun deviceManager(): DeviceManager
}
