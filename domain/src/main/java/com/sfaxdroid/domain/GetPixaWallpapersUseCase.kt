package com.sfaxdroid.domain

import com.sfaxdroid.data.mappers.PixaResponse
import com.sfaxdroid.data.repositories.ApiService
import javax.inject.Inject
import javax.inject.Named

const val pixaBaySearchTerm = "landscape"
const val pixaBayImageType = "photo"
const val pixaBayPerPage = "200"
const val pixaBayCategory = "nature"
const val pixaBaySafeSearch = "true"
class GetPixaWallpapersUseCase
    @Inject constructor(
        @Named("pixabay-key") private val pixaBayApiKey: String
    ) {
    suspend fun getResult(): PixaResponse {
        val apiService = ApiService.getInstance()
        val response = apiService.getImages(
            pixaBayApiKey,
            pixaBaySearchTerm,
            pixaBayImageType,
            pixaBayPerPage,
            pixaBayCategory,
            pixaBaySafeSearch
        )
        return response
    }
}