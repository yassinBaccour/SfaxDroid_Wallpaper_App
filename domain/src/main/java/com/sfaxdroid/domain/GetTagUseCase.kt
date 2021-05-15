package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.data.repositories.WsRepository
import javax.inject.Inject

class GetTagUseCase @Inject constructor(private val wsRepository: WsRepository) :
    ResultUseCase<GetTagUseCase.Param, Response>() {

    override suspend fun doWork(params: Param): Response {
        return wsRepository.getTags(params.screenName)
    }

    class Param(
        val screenName: String
    )


}
