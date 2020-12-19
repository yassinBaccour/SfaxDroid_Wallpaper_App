package com.sfaxdroid.data;

import com.sfaxdroid.base.WallpapersRetrofitObject;

import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * Created by yassin baccour on 10/04/2017.
 */

public interface WallpaperApiService {
    @GET("wallpapersRetrofitFormat.xml")
    Flowable<WallpapersRetrofitObject> getWallpapersFromServices();
}
