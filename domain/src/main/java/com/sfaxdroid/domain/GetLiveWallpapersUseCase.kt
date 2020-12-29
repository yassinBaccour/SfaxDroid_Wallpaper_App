package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.data.repositories.WsRepository
import javax.inject.Inject

class GetLiveWallpapersUseCase @Inject constructor(private val wsRepository: WsRepository) :
    UseCase<Response, GetLiveWallpapersUseCase.Param>() {
    override suspend fun run(params: Param): Response {
        return wsRepository.getLiveWallpapers(params.file)
    }

    class Param(
        val file: String
    )
}