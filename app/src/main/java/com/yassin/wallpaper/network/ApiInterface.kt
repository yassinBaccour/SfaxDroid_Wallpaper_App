package com.yassin.wallpaper.network

import com.sfaxdroid.bases.Constants.Companion.PIXA_API_KEY
import com.yassin.wallpaper.Model.PixaResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("?key=$PIXA_API_KEY")

    suspend fun getImages(
        @Query("q") searchTerm: String,
        @Query("image_type") imageType: String,
        //@Query("orientation") orientation:String,
        @Query("per_page") perPage: String,
        @Query("category") category: String,
        @Query("safesearch") safeSearch: String
    ): PixaResponse

    companion object {
        const val BASE_URL = "https://pixabay.com/api/"
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