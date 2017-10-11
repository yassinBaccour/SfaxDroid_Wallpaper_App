package com.sami.rippel.allah;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.sami.rippel.model.MyService;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.utils.DataUtils;
import com.sami.rippel.utils.FileUtils;
import com.sami.rippel.utils.MyDevice;

import java.util.HashSet;
import java.util.Set;
//import com.squareup.leakcanary.LeakCanary;

/**
 * Created by yassin baccour on 15/05/2016.
 */

public class WallpaperApplication extends MultiDexApplication {

    private Tracker mTracker;
    private Set<Activity> allActivities;
    private static WallpaperApplication instance;


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    public static synchronized WallpaperApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //InitCanary();
        ViewModel.Current = new ViewModel(new MyDevice(getApplicationContext()),
                new FileUtils(getApplicationContext()),
                new MyService(),
                new DataUtils(getApplicationContext()));
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
