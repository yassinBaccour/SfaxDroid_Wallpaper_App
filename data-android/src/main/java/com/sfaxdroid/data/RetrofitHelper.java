package com.sfaxdroid.data;

import com.sfaxdroid.base.WallpapersRetrofitObject;

import io.reactivex.Flowable;

/**
 * Created by yassin baccour on 03/05/2017.
 */

public class RetrofitHelper {

    private WallpaperApiService wallpaperApiService;

    public RetrofitHelper(WallpaperApiService wallpaperApiService) {
        this.wallpaperApiService = wallpaperApiService;
    }

    public Flowable<WallpapersRetrofitObject> getWallpapersList() {
        return wallpaperApiService.getWallpapersFromServices();
    }

}
