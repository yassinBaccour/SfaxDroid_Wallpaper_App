package com.sami.rippel.model;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sami.rippel.model.api.service.WallpaperApiService;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by yassin baccour on 10/04/2017.
 */

public class MyService {
    public Context context;
    public Retrofit retrofit;
    public RetrofitHelper mRetrofitHelper;

    public MyService()
    {
        mRetrofitHelper = new RetrofitHelper(getWallpaperApiService());
    }

    /*
    public static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create()).build();
    }

    public static WallpaperApiService getApiService() {
        return getRetrofitInstance().create(WallpaperApiService.class);
    }
    */

    public WallpaperApiService getWallpaperApiService() {
        return createRetrofitXMlConverter().create(WallpaperApiService.class);
    }

    private Retrofit createRetrofitXMlConverter() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //Pour info j'utilise l'adapter de jakewharton
                .addConverterFactory(SimpleXmlConverterFactory.create()).build();  //Comme convertisseur j'utilise celui du xml converter
        return retrofit;
    }
}
