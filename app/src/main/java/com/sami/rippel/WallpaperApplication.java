package com.sami.rippel;

import androidx.multidex.MultiDexApplication;

import com.sami.rippel.model.ViewModel;
import com.sfaxdroid.base.SharedPrefsUtils;
import com.sfaxdroid.data.MyService;

/**
 * Created by yassin baccour on 15/05/2016.
 */

public class WallpaperApplication extends MultiDexApplication {

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
    public void onTerminate() {
        super.onTerminate();
        ViewModel.Current = null;
    }

}
