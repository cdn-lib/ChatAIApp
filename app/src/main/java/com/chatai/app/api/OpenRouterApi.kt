package com.chatai.app.api

import com.chatai.app.model.OpenRouterRequest
import com.chatai.app.model.OpenRouterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenRouterApi {

    @POST("chat/completions")
    suspend fun sendMessage(
        @Body request: OpenRouterRequest
    ): Response<OpenRouterResponse>
}
