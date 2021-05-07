package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.data.repositories.WsRepository
import javax.inject.Inject

class GetLabUseCase @Inject constructor(private val wsRepository: WsRepository) :
    UseCase<Response, GetLabUseCase.Param>() {
    override suspend fun run(params: Param): Response {
        return wsRepository.getLab(params.file)
    }

    class Param(
        val file: String
    )
}
