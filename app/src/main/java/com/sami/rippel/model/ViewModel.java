package com.sami.rippel.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sami.rippel.model.entity.IntentTypeEnum;
import com.sami.rippel.model.entity.StateEnum;
import com.sami.rippel.model.entity.WallpaperCategory;
import com.sami.rippel.model.entity.WallpaperObject;
import com.sami.rippel.model.entity.WallpapersRetrofitObject;
import com.sami.rippel.model.listner.OnStateChangeListener;
import com.sami.rippel.utils.DataUtils;
import com.sami.rippel.utils.FileUtils;
import com.sami.rippel.utils.MyDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yassin baccour on 30/10/2016.
 */

public class ViewModel {
    public static ViewModel Current;
    private final List<OnStateChangeListener> mStateListeners = new ArrayList<>();
    public FileUtils fileUtils;
    public MyDevice device;
    public MyService service;
    public DataUtils dataUtils;
    public WallpapersRetrofitObject retrofitWallpObject;
    protected StateEnum mState = StateEnum.COMPLETED;

    public ViewModel(MyDevice device, FileUtils fileUtils, MyService service, DataUtils dataUtils) {
        this.device = device;
        this.fileUtils = fileUtils;
        this.service = service;
        this.dataUtils = dataUtils;
        ViewModel.Current = this;
    }

    public String GetIntentNameFromType(IntentTypeEnum intentType) {
        if (intentType == IntentTypeEnum.FACEBOOKINTENT) {
            return "com.facebook.katana";
        } else if (intentType == IntentTypeEnum.INTAGRAMINTENT) {
            return "com.instagram.android";
        } else if (intentType == IntentTypeEnum.SHNAPCHATINTENT) {
            return "com.snapchat.android";
        } else
            return null;
    }

    public Bitmap setReducedImageSize(String imagePath) {

        int targetImageViewWidth = Current.device.getScreenHeightPixels();
        int targetImageViewHeight = Current.device.getScreenWidthPixels();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;
        int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, bmOptions);
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

    public String GetBytesDownloaded(int progress, long totalBytes) {
        long bytesCompleted = (progress * totalBytes) / 100;
        if (totalBytes >= 1000000) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000000)) + "MB");
        }
        if (totalBytes >= 1000) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000)) + "Kb");

        } else {
            return ("" + bytesCompleted + "/" + totalBytes);
        }
    }

    public String getUrlFromWallpaperList(int pos, List<WallpaperObject> myList) {
        String url = "";
        if (ViewModel.Current.device.getScreenHeightPixels() < Constants.MIN_HEIGHT && ViewModel.Current.device.getScreenWidthPixels() < Constants.MIN_WIDHT)
            url = myList.get(pos).getPreviewUrl().replace("/islamicimages/", "/islamicimagesmini/");
        else
            url = myList.get(pos).getPreviewUrl();
        return url;
    }

    public String getUrlFromWallpaper(WallpaperObject wall) {
        String url = "";
        if (ViewModel.Current.device.getScreenHeightPixels() < Constants.MIN_HEIGHT && ViewModel.Current.device.getScreenWidthPixels() < Constants.MIN_WIDHT)
            url = wall.getPreviewUrl().replace("/islamicimages/", "/islamicimagesmini/");
        else
            url = wall.getPreviewUrl();
        return url;
    }

    public String getUrlByScreenSize(String urlToChange) {
        String url = "";
        if (ViewModel.Current.device.getScreenHeightPixels() < Constants.MIN_HEIGHT && ViewModel.Current.device.getScreenWidthPixels() < Constants.MIN_WIDHT)
            url = urlToChange.replace("/islamicimages/", "/islamicimagesmini/");
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
