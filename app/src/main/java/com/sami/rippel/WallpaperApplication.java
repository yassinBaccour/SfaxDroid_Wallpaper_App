package com.sami.rippel;

import androidx.multidex.MultiDexApplication;

import com.sami.rippel.core.DaggerApplicationComponent;
import com.sami.rippel.model.ViewModel;
import com.sfaxdroid.base.SharedPrefsUtils;
import com.sfaxdroid.data.MyService;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by yassin baccour on 15/05/2016.
 */

public class WallpaperApplication extends DaggerApplication {

    private static WallpaperApplication instance;

    public static synchronized WallpaperApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ViewModel.Current = new ViewModel(
                new MyService(),
                new SharedPrefsUtils(getApplicationContext())
        );
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.factory().create(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ViewModel.Current = null;
    }

}
