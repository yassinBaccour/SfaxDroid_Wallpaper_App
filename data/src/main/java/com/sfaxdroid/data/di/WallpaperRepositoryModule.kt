package com.sfaxdroid.data.di

import com.sfaxdroid.data.repository.PartnerRepositoryImpl
import com.sfaxdroid.data.repository.SfaxDroidServerRepositoryImpl
import com.sfaxdroid.domain.repository.PartnerServerRepository
import com.sfaxdroid.domain.repository.SfaxDroidServerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WallpaperRepositoryModule {

    @Singleton
    @Binds
    abstract fun providePartnerUrlRepository(bind: PartnerRepositoryImpl): PartnerServerRepository

    @Singleton
    @Binds
    abstract fun provideFmmRepository(bind: SfaxDroidServerRepositoryImpl): SfaxDroidServerRepository

}