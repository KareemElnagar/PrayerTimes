package com.kareem.prayertimes.data.repo.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kareem.prayertimes.data.model.Data

class Converters {
    private val gson = Gson()
    @TypeConverter
    fun fromDataList(dataList: List<Data>): String {

        return gson.toJson(dataList)

    }
    @TypeConverter
    fun toDataList(dataString: String): List<Data> {
        val type = object : TypeToken<List<Data>>() {}.type
        return gson.fromJson(dataString, type)
    }

}