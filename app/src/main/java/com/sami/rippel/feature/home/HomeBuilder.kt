package com.sami.rippel.feature.home

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeBuilder {

    @ContributesAndroidInjector
    internal abstract fun homeActivity(): HomeActivity

    @ContributesAndroidInjector
    internal abstract fun homeActivityNavBar(): HomeActivityNavBar
}