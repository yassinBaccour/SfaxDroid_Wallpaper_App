package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.data.repositories.WsRepository
import javax.inject.Inject

class GetAllWallpapersUseCase @Inject constructor(private val wsRepository: WsRepository) :
    UseCase<Response, GetAllWallpapersUseCase.Param>() {
    override suspend fun run(params: Param): Response {
        return wsRepository.getAllWallpaper(params.file)
    }

    class Param(
        val file: String
    )
}