package com.sfaxdroid.list.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.data.mappers.PixaResponse
import com.sfaxdroid.data.repositories.ApiService
import kotlinx.coroutines.launch

const val pixaBaySearchTerm = "landscape"
const val pixaBayImageType = "photo"
const val pixaBayPerPage = "200"
const val pixaBayCategory = "nature"
const val pixaBaySafeSearch = "true"

class PictureViewModel : ViewModel() {

    var pixaApiResponse: PixaResponse by mutableStateOf(PixaResponse(0, 0, listOf()))
    var errorMessage: String by mutableStateOf("")
    fun getPictureList() {
        viewModelScope.launch {
            val apiService = ApiService.getInstance()
            try {
                val response = apiService.getImages(
                    pixaBaySearchTerm,
                    pixaBayImageType,
                    pixaBayPerPage,
                    pixaBayCategory,
                    pixaBaySafeSearch
                )
                pixaApiResponse = response
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}