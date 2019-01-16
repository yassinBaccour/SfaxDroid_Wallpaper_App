package com.sami.rippel.model;

import com.sami.rippel.model.entity.UpdateApp;
import com.sami.rippel.model.entity.WallpapersRetrofitObject;

import io.reactivex.Flowable;

/**
 * Created by yassin baccour on 03/05/2017.
 */

public class RetrofitHelper {

    private WallpaperApiService wallpaperApiService;

    public RetrofitHelper(WallpaperApiService wallpaperApiService) {
        this.wallpaperApiService = wallpaperApiService;
    }

    public WallpaperApiService getWallpaperApiService() {
        return wallpaperApiService;
    }

    public Flowable<WallpapersRetrofitObject> getWallpapersList() {
        return wallpaperApiService.getWallpapersFromServices();
    }

    public Flowable<UpdateApp> getUpdateObject() {
        return wallpaperApiService.isUpdateNeeded();
    }
}
