package com.kareem.prayertimes.domain.usecase

import com.kareem.prayertimes.data.model.PrayerTimeRes
import com.kareem.prayertimes.domain.repo.repo
import retrofit2.Response
import javax.inject.Inject

class GetPrayerTimesUseCase @Inject constructor(private val repo: repo) {
    suspend operator fun invoke(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
        method: Int
    ): Response<PrayerTimeRes> = repo.getPrayerTimes(year,month,latitude,longitude,method)


}