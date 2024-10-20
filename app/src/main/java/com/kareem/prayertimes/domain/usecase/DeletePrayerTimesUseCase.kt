package com.kareem.prayertimes.domain.usecase

import com.kareem.prayertimes.domain.repo.repo
import javax.inject.Inject

class DeletePrayerTimesUseCase @Inject constructor(private val repo : repo) {
    suspend operator fun invoke() = repo.deletePrayerTimes()

}