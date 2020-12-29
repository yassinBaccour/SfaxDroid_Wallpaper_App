package com.sami.rippel.core

import com.sami.rippel.feature.main.fragments.AllBackgroundFragment
import com.sami.rippel.feature.main.fragments.CategoryFragment
import com.sami.rippel.feature.main.fragments.LabFragment
import com.sami.rippel.feature.main.fragments.LwpFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun allBackgroundFragment(): AllBackgroundFragment

    @ContributesAndroidInjector
    internal abstract fun CategoryFragment(): CategoryFragment

    @ContributesAndroidInjector
    internal abstract fun labFragment(): LabFragment

    @ContributesAndroidInjector
    internal abstract fun lwpFragment(): LwpFragment

}