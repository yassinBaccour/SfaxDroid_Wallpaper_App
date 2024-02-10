package com.sfaxdroid.list.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.data.mappers.PixaResponse
import com.sfaxdroid.data.repositories.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val pixaBaySearchTerm = "landscape"
const val pixaBayImageType = "photo"
const val pixaBayPerPage = "200"
const val pixaBayCategory = "nature"
const val pixaBaySafeSearch = "true"

@HiltViewModel
class PixaViewModel
    @Inject constructor(
        @Named("pixabay-key") private val pixaBayApiKey: String
) : ViewModel() {

    var pixaApiResponse: PixaResponse by mutableStateOf(PixaResponse(0, 0, listOf()))
    var errorMessage: String by mutableStateOf("")

    fun getPictureList() {
        viewModelScope.launch {
            val apiService = ApiService.getInstance()
            try {
                val response = apiService.getImages(
                    pixaBayApiKey,
                    pixaBaySearchTerm,
                    pixaBayImageType,
                    pixaBayPerPage,
                    pixaBayCategory,
                    pixaBaySafeSearch
                )
                pixaApiResponse = response
                Log.d("PixaResponse", response.toString())
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                Log.d("PixaResponse", errorMessage)
            }
        }
    }
}