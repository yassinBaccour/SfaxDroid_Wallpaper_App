package com.sami.rippel.model;

import com.sami.rippel.model.entity.IntentTypeEnum;
import com.sami.rippel.model.entity.StateEnum;
import com.sami.rippel.model.entity.WallpaperCategory;
import com.sami.rippel.model.entity.WallpaperObject;
import com.sami.rippel.model.entity.WallpapersRetrofitObject;
import com.sami.rippel.model.listner.OnStateChangeListener;
import com.sami.rippel.utils.DeviceUtils;
import com.sami.rippel.utils.FileUtils;
import com.sami.rippel.utils.SharedPrefsUtils;
import com.sfaxdroid.base.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yassin baccour on 30/10/2016.
 */

public class ViewModel {
    public static ViewModel Current;
    private final List<OnStateChangeListener> mStateListeners = new ArrayList<>();
    public FileUtils fileUtils;
    public DeviceUtils device;
    public MyService service;
    public SharedPrefsUtils sharedPrefsUtils;
    public WallpapersRetrofitObject retrofitWallpObject;
    protected StateEnum mState = StateEnum.COMPLETED;

    public ViewModel(DeviceUtils device, FileUtils fileUtils, MyService service, SharedPrefsUtils sharedPrefsUtils) {
        this.device = device;
        this.fileUtils = fileUtils;
        this.service = service;
        this.sharedPrefsUtils = sharedPrefsUtils;
        ViewModel.Current = this;
    }

    public String GetIntentNameFromType(IntentTypeEnum intentType) {
        if (intentType == IntentTypeEnum.FACEBOOKINTENT) {
            return Constants.FB_PACKAGE;
        } else if (intentType == IntentTypeEnum.INTAGRAMINTENT) {
            return Constants.INSTAGRAM_PACKAGE;
        } else if (intentType == IntentTypeEnum.SHNAPCHATINTENT) {
            return Constants.SNAP_PACKAGE;
        } else
            return null;
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

    public String getUrlFromWallpaperList(int pos, List<WallpaperObject> myList) {
        String url = "";
        if (ViewModel.Current.device.getScreenHeightPixels() < Constants.MIN_HEIGHT && ViewModel.Current.device.getScreenWidthPixels() < Constants.MIN_WIDHT)
            url = myList.get(pos).getPreviewUrl().replace(Constants.URL_BIG_WALLPAPER_FOLDER, Constants.URL_SMALL_WALLPAPER_FOLDER);
        else
            url = myList.get(pos).getPreviewUrl();
        return url;
    }

    public String getUrlFromWallpaper(WallpaperObject wall) {
        String url = "";
        if (ViewModel.Current.device.getScreenHeightPixels() < Constants.MIN_HEIGHT && ViewModel.Current.device.getScreenWidthPixels() < Constants.MIN_WIDHT)
            url = wall.getPreviewUrl().replace(Constants.URL_BIG_WALLPAPER_FOLDER, Constants.URL_SMALL_WALLPAPER_FOLDER);
        else
            url = wall.getPreviewUrl();
        return url;
    }

    public String getUrlByScreenSize(String urlToChange) {
        String url = "";
        if (ViewModel.Current.device.getScreenHeightPixels() < Constants.MIN_HEIGHT && ViewModel.Current.device.getScreenWidthPixels() < Constants.MIN_WIDHT)
            url = urlToChange.replace(Constants.URL_BIG_WALLPAPER_FOLDER, Constants.URL_SMALL_WALLPAPER_FOLDER);
        else
            url = urlToChange;
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
