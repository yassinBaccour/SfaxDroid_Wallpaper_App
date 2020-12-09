package com.sami.rippel.model;

import android.content.Context;

import com.sami.rippel.model.entity.StateEnum;
import com.sami.rippel.model.listner.OnStateChangeListener;
import com.sami.rippel.utils.FileUtils;
import com.sfaxdroid.base.SharedPrefsUtils;
import com.sfaxdroid.base.Utils;
import com.sfaxdroid.base.WallpaperObject;
import com.sfaxdroid.base.WallpaperCategory;
import com.sfaxdroid.base.WallpapersRetrofitObject;
import com.sfaxdroid.data.MyService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yassin baccour on 30/10/2016.
 */

public class ViewModel {

    public static ViewModel Current;
    private final List<OnStateChangeListener> mStateListeners = new ArrayList<>();
    public FileUtils fileUtils;
    public MyService service;
    public SharedPrefsUtils sharedPrefsUtils;
    public WallpapersRetrofitObject retrofitWallpObject;
    protected StateEnum mState = StateEnum.COMPLETED;

    public ViewModel(MyService service, SharedPrefsUtils sharedPrefsUtils) {
        this.service = service;
        this.sharedPrefsUtils = sharedPrefsUtils;
        ViewModel.Current = this;
    }

    public WallpapersRetrofitObject getRetrofitWallpObject() {
        return retrofitWallpObject;
    }

    public void setRetrofitWallpObject(WallpapersRetrofitObject retrofitWallpObject) {
        this.retrofitWallpObject = retrofitWallpObject;
        onStateChange();
    }

    //Save State listner to update List when retrofitWallpObject is updated
    public void registerOnStateChangeListener(OnStateChangeListener listener) {
        synchronized (mStateListeners) {
            if (!mStateListeners.contains(listener)) {
                mStateListeners.add(listener);
            }
        }
    }

    public void unregisterOnStateChangeListener(OnStateChangeListener listener) {
        synchronized (mStateListeners) {
            if (mStateListeners.contains(listener)) {
                mStateListeners.remove(listener);
            }
        }
    }

    protected void onStateChange() {
        synchronized (mStateListeners) {
            for (OnStateChangeListener listener : mStateListeners) {
                listener.onStateChange(mState);
            }
        }
    }

    public boolean isWallpapersLoaded() {
        return retrofitWallpObject != null && retrofitWallpObject.getCategoryList().size() > 0;
    }

    public String getUrlFromWallpaper(WallpaperObject wall, Context context) {
        String url = "";
        if (Utils.Companion.getScreenHeightPixels(context) < com.sfaxdroid.base.Constants.MIN_HEIGHT && Utils.Companion.getScreenWidthPixels(context) < com.sfaxdroid.base.Constants.MIN_WIDHT)
            url = wall.getPreviewUrl().replace(com.sfaxdroid.base.Constants.URL_BIG_WALLPAPER_FOLDER, com.sfaxdroid.base.Constants.URL_SMALL_WALLPAPER_FOLDER);
        else
            url = wall.getPreviewUrl();
        return url;
    }

    public WallpaperObject GetWallpaperFromCategoryNameAndPos(String catName, String position) {
        List<WallpaperCategory> wall = ViewModel.Current.retrofitWallpObject.getCategoryList();
        for (WallpaperCategory wallCat : wall) {
            if (wallCat.getTitle().equals(catName)) {
                List<WallpaperObject> wallobj = wallCat.getGetWallpapersList();
                for (WallpaperObject x : wallobj) {
                    if (x.getName().equals(position))
                        return x;
                }
            }
        }
        return null;
    }

    public WallpaperCategory getWallpaperCategoryFromName(String catName) {
        List<WallpaperCategory> wall = ViewModel.Current.retrofitWallpObject.getCategoryList();
        for (WallpaperCategory wallCat : wall) {
            if (wallCat.getTitle().equals(catName)) {
                return wallCat;
            }
        }
        return null;
    }


}
