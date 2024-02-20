package com.sfaxdroid.domain

import com.sfaxdroid.data.entity.PixaTagWithSearchData
import com.sfaxdroid.data.mappers.PixaItem
import com.sfaxdroid.data.repositories.ApiService
import javax.inject.Inject
import javax.inject.Named

const val pixaBayImageType = "photo"
const val pixaBayPerPage = "200"
const val pixaBaySafeSearch = "true"
const val pixaBayPage = "1"

class GetPixaWallpapersUseCase
@Inject
constructor(@Named("pixabay-key") private val pixaBayApiKey: String) {
    suspend fun getResult(pixaTagWithSearchData: PixaTagWithSearchData): List<PixaItem> {
        return ApiService.getInstance()
            .getImages(
                apiKey = pixaBayApiKey,
                category = pixaTagWithSearchData.search.category,
                imageType = pixaBayImageType,
                perPage = pixaBayPerPage,
                searchTerm = pixaTagWithSearchData.search.searchTerm,
                safeSearch = pixaBaySafeSearch,
                page = pixaBayPage
            ).hits
    }
}
