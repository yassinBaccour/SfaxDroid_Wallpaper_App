package com.sami.rippel;

import androidx.multidex.MultiDexApplication;

import com.sami.rippel.model.MyService;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.utils.DeviceUtils;
import com.sami.rippel.utils.FileUtils;
import com.sami.rippel.utils.SharedPrefsUtils;

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
                new DeviceUtils(getApplicationContext()),
                new FileUtils(getApplicationContext()),
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
