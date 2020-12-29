package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.data.repositories.WsRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(private val wsRepository: WsRepository) :
    UseCase<Response, GetCategoryUseCase.Param>() {
    override suspend fun run(params: Param): Response {
        return wsRepository.getCategory(params.file)
    }

    class Param(
        val file: String
    )
}