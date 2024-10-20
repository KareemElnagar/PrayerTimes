package com.kareem.prayertimes.domain.usecase

import com.kareem.prayertimes.data.model.PrayerTimeRes
import com.kareem.prayertimes.domain.repo.repo
import javax.inject.Inject

class SavePrayerTimesUseCase @Inject constructor(private val repo: repo) {
    suspend operator fun invoke(
        response: PrayerTimeRes
    ): Long = repo.savePrayerTimes(response)
}