package com.yassin.wallpaper.network

import com.yassin.wallpaper.Model.PixaPicture
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/api")

    suspend fun getImages(
        @Query("key") apiKey:String,
        @Query("q") searchTerm:String,
        @Query("image_type") imageType:String,
        @Query("orientation") orientation:String):  List<PixaPicture>
    companion object{
        const val BASE_URL = "https://pixabay.com"
        var apiService: ApiService? = null
        fun getInstance(): ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }
            return apiService!!
        }
    }

}