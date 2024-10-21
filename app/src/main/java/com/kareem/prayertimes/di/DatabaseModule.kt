package com.kareem.prayertimes.di

import android.content.Context
import androidx.room.Room
import com.kareem.prayertimes.data.repo.local.PrayerDao
import com.kareem.prayertimes.data.repo.local.PrayerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providePrayerDatabase(@ApplicationContext context: Context): PrayerDatabase {
        return Room.databaseBuilder(
            context,
            PrayerDatabase::class.java,
            "prayer_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePrayerDao(prayerDatabase: PrayerDatabase): PrayerDao {
        return prayerDatabase.dao
    }


}