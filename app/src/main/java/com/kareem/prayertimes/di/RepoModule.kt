package com.kareem.prayertimes.di

import com.kareem.prayertimes.data.repo.RepoImpl
import com.kareem.prayertimes.data.repo.local.PrayerDatabase
import com.kareem.prayertimes.data.repo.remote.ApiService
import com.kareem.prayertimes.domain.repo.repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideRepository(
        api: ApiService,
        db: PrayerDatabase
    ): repo {
        return RepoImpl(api,db)

    }
}