package com.kareem.prayertimes.di

import com.kareem.prayertimes.domain.repo.repo
import com.kareem.prayertimes.domain.usecase.DeletePrayerTimesUseCase
import com.kareem.prayertimes.domain.usecase.GetAllPrayersTimesUseCase
import com.kareem.prayertimes.domain.usecase.GetPrayerTimesUseCase
import com.kareem.prayertimes.domain.usecase.GetQiblaDirectionUseCase
import com.kareem.prayertimes.domain.usecase.PrayerUseCases
import com.kareem.prayertimes.domain.usecase.SavePrayerTimesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun provideUseCases(repo: repo): PrayerUseCases {
        return PrayerUseCases(
            GetPrayerTimesUseCase(repo),
            GetAllPrayersTimesUseCase(repo),
            GetQiblaDirectionUseCase(repo),
            SavePrayerTimesUseCase(repo),
            DeletePrayerTimesUseCase(repo),

        )
    }

}