package com.streamwave.radio.network.api

import com.streamwave.radio.data.database.entity.CategoryEntity
import com.streamwave.radio.data.database.entity.OfficialStationEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StationApiService {
    @GET("api/stations")
    suspend fun getAllStations(): Response<List<OfficialStationEntity>>

    @GET("api/stations/featured")
    suspend fun getFeaturedStations(): Response<List<OfficialStationEntity>>

    @GET("api/stations/{id}")
    suspend fun getStation(@Path("id") id: Long): Response<OfficialStationEntity>

    @GET("api/categories")
    suspend fun getCategories(): Response<List<CategoryEntity>>
}
