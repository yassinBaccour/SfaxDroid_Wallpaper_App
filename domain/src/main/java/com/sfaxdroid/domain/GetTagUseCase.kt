package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.data.repositories.WsRepository
import javax.inject.Inject

class GetTagUseCase @Inject constructor(private val wsRepository: WsRepository) :
    UseCase<Response, GetTagUseCase.Param>() {
    override suspend fun run(params: Param): Response {
        return wsRepository.getTags(params.screenName)
    }

    class Param(
        val screenName: String
    )
}
