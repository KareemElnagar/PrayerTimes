package com.kareem.prayertimes.data.repo

import androidx.lifecycle.LiveData
import com.kareem.prayertimes.data.model.PrayerTimeRes
import com.kareem.prayertimes.data.model.qibla.QiblaResponse
import com.kareem.prayertimes.data.repo.local.PrayerDatabase
import com.kareem.prayertimes.data.repo.remote.ApiService
import com.kareem.prayertimes.domain.repo.repo
import retrofit2.Response
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val db: PrayerDatabase
): repo {
    override suspend fun getPrayerTimes(
        latitude: Double,
        longitude: Double,
        year: Int,
        month: Int,
        method: Int
    ): Response<PrayerTimeRes> = apiService.getPrayerTimes(year, month, latitude, longitude, method)


    override  fun getAllPrayerTimes(): LiveData<PrayerTimeRes> = db.dao.getAllPrayerTimes()

    override suspend fun getQiblaDirection(
        latitude: Double,
        longitude: Double
    ): Response<QiblaResponse> = apiService.getQiblaDirection(latitude, longitude)

    override suspend fun savePrayerTimes(response: PrayerTimeRes): Long = db.dao.savePrayerTimes(response)

    override suspend fun deletePrayerTimes() = db.dao.deleteAllPrayerTimes()
}