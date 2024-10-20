package com.kareem.prayertimes.domain.usecase

data class PrayerUseCases(
    val getPrayerTimes: GetPrayerTimesUseCase,
    val getAllPrayersTimesUseCase: GetAllPrayersTimesUseCase,
    val getQiblaDirectionUseCase: GetQiblaDirectionUseCase,
    val savePrayerTimesUseCase: SavePrayerTimesUseCase,
    val deletePrayerTimesUseCase: DeletePrayerTimesUseCase,

)
