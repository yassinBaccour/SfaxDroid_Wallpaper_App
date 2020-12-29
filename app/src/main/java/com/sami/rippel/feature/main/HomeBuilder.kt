package com.sami.rippel.feature.main

import com.sami.rippel.feature.main.activity.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeBuilder {

    @ContributesAndroidInjector
    internal abstract fun homeActivity(): HomeActivity
}