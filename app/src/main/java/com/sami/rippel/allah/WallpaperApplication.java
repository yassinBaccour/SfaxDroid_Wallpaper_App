package com.sami.rippel.allah;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.sami.rippel.model.MyService;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.utils.BitmapUtils;
import com.sami.rippel.utils.SharedPrefsUtils;
import com.sami.rippel.utils.FileUtils;
import com.sami.rippel.utils.DeviceUtils;

import java.util.HashSet;
import java.util.Set;
//import com.squareup.leakcanary.LeakCanary;

/**
 * Created by yassin baccour on 15/05/2016.
 */

public class WallpaperApplication extends MultiDexApplication {

    private static WallpaperApplication instance;
    private Tracker mTracker;
    private Set<Activity> allActivities;

    public static synchronized WallpaperApplication getInstance() {
        return instance;
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
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
        mTracker = null;
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
