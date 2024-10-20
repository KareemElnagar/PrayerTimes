package com.kareem.prayertimes.data.repo.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kareem.prayertimes.data.model.PrayerTimeRes

@Database(
    entities = [PrayerTimeRes::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PrayerDatabase : RoomDatabase() {
    abstract val dao: PrayerDao

    companion object {
        @Volatile
        private var daoInstance: PrayerDao? = null

        private fun buildDatabase(context: Context): PrayerDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                PrayerDatabase::class.java,
                "prayer_database"
            ).fallbackToDestructiveMigration().build()

        private fun getDaoInstance(context: Context): PrayerDao {
            synchronized(this) {
                if (daoInstance == null) {
                    daoInstance = buildDatabase(context).dao
                }
                return daoInstance as PrayerDao
            }
        }
    }
}