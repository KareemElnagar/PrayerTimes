package com.kareem.prayertimes.domain.usecase

import androidx.lifecycle.LiveData
import com.kareem.prayertimes.data.model.PrayerTimeRes
import com.kareem.prayertimes.domain.repo.repo
import javax.inject.Inject

class GetAllPrayersTimesUseCase @Inject constructor(private val repo: repo) {
      operator fun invoke(): LiveData<PrayerTimeRes> = repo.getAllPrayerTimes()
}