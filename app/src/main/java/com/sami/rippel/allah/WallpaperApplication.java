package com.sami.rippel.allah;

import com.sami.rippel.model.MyService;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.utils.BitmapUtils;
import com.sami.rippel.utils.DeviceUtils;
import com.sami.rippel.utils.FileUtils;
import com.sami.rippel.utils.SharedPrefsUtils;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yassin baccour on 15/05/2016.
 */

public class WallpaperApplication extends MultiDexApplication {

    private static WallpaperApplication instance;

    private Set<Activity> allActivities;

    public static synchronized WallpaperApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ViewModel.Current = new ViewModel(new DeviceUtils(getApplicationContext()),
                new FileUtils(getApplicationContext()),
                new MyService(),
                new SharedPrefsUtils(getApplicationContext()),
                new BitmapUtils());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ViewModel.Current = null;
    }

    public void InitCanary() {
        /*
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        */
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
