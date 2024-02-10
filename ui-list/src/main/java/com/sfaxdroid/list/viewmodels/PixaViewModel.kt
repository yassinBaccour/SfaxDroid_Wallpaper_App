package com.sfaxdroid.list.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.data.mappers.PixaResponse
import com.sfaxdroid.data.repositories.ApiService
import com.sfaxdroid.domain.GetPixaWallpapersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named



@HiltViewModel
class PixaViewModel
    @Inject constructor(
        private val getPixaWallpapersUseCase: GetPixaWallpapersUseCase
) : ViewModel() {

    var pixaApiResponse: PixaResponse by mutableStateOf(PixaResponse(0, 0, listOf()))
    //var errorMessage: String by mutableStateOf("")

    fun getPictureList() {
        viewModelScope.launch {
            pixaApiResponse = getPixaWallpapersUseCase.getResult()
        }
    }
}