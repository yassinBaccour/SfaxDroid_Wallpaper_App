package com.sami.rippel.core.di

import com.sami.rippel.feature.home.AllInOneFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun allInOneFragment(): AllInOneFragment

}