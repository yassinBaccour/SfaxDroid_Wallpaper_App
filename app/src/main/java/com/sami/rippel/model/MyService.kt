package com.sami.rippel.model

import android.content.Context
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

/**
 * Created by yassin baccour on 10/04/2017.
 */
class MyService {
    var context: Context? = null

    @JvmField
    var mRetrofitHelper: RetrofitHelper
    private val wallpaperApiService: WallpaperApiService
        get() = createRetrofitXMlConverter().create(WallpaperApiService::class.java)

    init {
        mRetrofitHelper = RetrofitHelper(wallpaperApiService)
    }

    private fun createRetrofitXMlConverter(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(SimpleXmlConverterFactory.create()).build()
    }

}