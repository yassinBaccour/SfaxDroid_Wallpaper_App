package com.sami.rippel.core.di

import com.sami.rippel.WallpaperApplication
import com.sami.rippel.feature.home.HomeBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        HomeBuilder::class,
        FragmentBindingModule::class,
        ApplicationModule::class,
    ]
)
interface ApplicationComponent : AndroidInjector<WallpaperApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: WallpaperApplication): ApplicationComponent
    }
}