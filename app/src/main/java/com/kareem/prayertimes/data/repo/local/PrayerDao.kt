package com.kareem.prayertimes.data.repo.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kareem.prayertimes.data.model.PrayerTimeRes

@Dao
interface PrayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun savePrayerTimes(prayerTimeRes: PrayerTimeRes): Long

    @Query("SELECT * FROM prayer_time_table")
     fun getAllPrayerTimes(): LiveData<PrayerTimeRes>

    @Query("DELETE FROM prayer_time_table")
     fun deleteAllPrayerTimes()


}