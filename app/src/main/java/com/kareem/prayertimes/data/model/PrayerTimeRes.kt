package com.kareem.prayertimes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "prayer_time_table"
)
data class PrayerTimeRes(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val code: Int,
    val data: List<Data>,
    val status: String
)