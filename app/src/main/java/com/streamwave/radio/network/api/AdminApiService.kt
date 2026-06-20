package com.streamwave.radio.network.api

import com.streamwave.radio.data.database.entity.OfficialStationEntity
import com.streamwave.radio.data.database.entity.StationSubmissionEntity
import retrofit2.Response
import retrofit2.http.*

interface AdminApiService {
    @POST("api/admin/stations")
    suspend fun createStation(@Body station: OfficialStationEntity): Response<OfficialStationEntity>

    @PUT("api/admin/stations/{id}")
    suspend fun updateStation(@Path("id") id: Long, @Body station: OfficialStationEntity): Response<OfficialStationEntity>

    @DELETE("api/admin/stations/{id}")
    suspend fun deleteStation(@Path("id") id: Long): Response<Unit>

    @GET("api/admin/submissions")
    suspend fun getSubmissions(): Response<List<StationSubmissionEntity>>

    @PUT("api/admin/submissions/{id}")
    suspend fun reviewSubmission(
        @Path("id") id: Long,
        @Body body: Map<String, String>
    ): Response<StationSubmissionEntity>
}
