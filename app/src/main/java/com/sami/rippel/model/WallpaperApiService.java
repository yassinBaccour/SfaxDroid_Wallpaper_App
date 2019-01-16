package com.sami.rippel.model;

import com.sami.rippel.model.entity.UpdateApp;
import com.sami.rippel.model.entity.WallpapersRetrofitObject;

import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * Created by yassin baccour on 10/04/2017.
 */

public interface WallpaperApiService {
    @GET("wallpapersRetrofitFormat.xml")
    Flowable<WallpapersRetrofitObject> getWallpapersFromServices();

    @GET("needUpdate.xml")
    Flowable<UpdateApp> isUpdateNeeded();
}
