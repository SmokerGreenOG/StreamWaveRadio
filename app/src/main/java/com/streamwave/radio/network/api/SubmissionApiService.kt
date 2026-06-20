package com.streamwave.radio.network.api

import com.streamwave.radio.data.database.entity.StationSubmissionEntity
import retrofit2.Response
import retrofit2.http.*

interface SubmissionApiService {
    @POST("api/submissions")
    suspend fun submit(@Body submission: StationSubmissionEntity): Response<StationSubmissionEntity>

    @GET("api/submissions/{id}")
    suspend fun getStatus(@Path("id") id: Long): Response<StationSubmissionEntity>
}
