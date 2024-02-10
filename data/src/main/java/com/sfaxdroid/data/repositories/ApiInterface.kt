package com.sfaxdroid.data.repositories

import com.sfaxdroid.data.mappers.PixaResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



interface ApiService {


    @GET("/api/")
    suspend fun getImages(
        @Query("key") apiKey: String,
        @Query("q") searchTerm: String,
        @Query("image_type") imageType: String,
        @Query("per_page") perPage: String,
        @Query("category") category: String,
        @Query("safesearch") safeSearch: String
    ): PixaResponse

    companion object {
        private const val BASE_URL = "https://pixabay.com"
        private var apiService: ApiService? = null
        fun getInstance(): ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }
            return apiService?: throw IllegalStateException("ApiService not initialized")
        }
    }

}