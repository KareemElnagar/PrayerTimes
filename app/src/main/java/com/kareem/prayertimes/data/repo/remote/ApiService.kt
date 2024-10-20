package com.kareem.prayertimes.data.repo.remote

import com.kareem.prayertimes.data.model.PrayerTimeRes
import com.kareem.prayertimes.data.model.qibla.QiblaResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("calender/{year}/{month}")
    suspend fun getPrayerTimes(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int,
    ): Response<PrayerTimeRes>


    @GET("qibla/{latitude}/{longitude}")
    suspend fun getQiblaDirection(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
    ): Response<QiblaResponse>
}