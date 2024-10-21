package com.kareem.prayertimes.domain.repo

import androidx.lifecycle.LiveData
import com.kareem.prayertimes.data.model.PrayerTimeRes
import com.kareem.prayertimes.data.model.qibla.QiblaResponse
import retrofit2.Response

interface repo {

    suspend fun getPrayerTimes(
        latitude: Double,
        longitude: Double,
        year: Int,
        month: Int,
        method: Int
    ): Response<PrayerTimeRes>

    fun getAllPrayerTimes(): LiveData<PrayerTimeRes>
    suspend fun getQiblaDirection(latitude: Double, longitude: Double): Response<QiblaResponse>

    suspend fun savePrayerTimes(response: PrayerTimeRes) : Long


    suspend fun deletePrayerTimes()
}