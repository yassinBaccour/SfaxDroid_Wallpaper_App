package com.sami.rippel.core

import com.sami.rippel.feature.singleview.AllInOneFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun allInOneFragment(): AllInOneFragment

}