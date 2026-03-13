package com.biosync.app.util

import androidx.room.TypeConverter
import com.biosync.app.data.model.MentalEffort

class Converters {
    @TypeConverter
    fun fromMentalEffort(value: MentalEffort): String = value.name

    @TypeConverter
    fun toMentalEffort(value: String): MentalEffort = MentalEffort.valueOf(value)

    @TypeConverter
    fun fromTagList(tags: List<String>): String = tags.joinToString(",")

    @TypeConverter
    fun toTagList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }
}
