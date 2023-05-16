package com.example.timetable_application.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

data class OneTime(
    val hour:Int,
    val minute:Int
){
    override fun toString(): String {
        return String.format("%02d:%02d", hour, minute)
    }
}
@Entity
class OneClassTime(
    @PrimaryKey
    val id:Int,
    val startTime:OneTime,
    val endTime:OneTime,
){
    override fun toString(): String { return "$startTime - $endTime" }
}
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Int?): OneTime? {
        return value?.let { OneTime(it / 60, it % 60) }
    }

    @TypeConverter
    fun toTimestamp(time: OneTime?): Int? {
        return time?.let { it.hour * 60 + it.minute }
    }
}