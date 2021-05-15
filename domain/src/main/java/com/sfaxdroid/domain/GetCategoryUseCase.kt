package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.data.repositories.WsRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(private val wsRepository: WsRepository) :
    ResultUseCase<GetCategoryUseCase.Param, Response>() {

    override suspend fun doWork(params: Param): Response {
        return wsRepository.getCategory(params.file)
    }

    class Param(
        val file: String
    )

}
