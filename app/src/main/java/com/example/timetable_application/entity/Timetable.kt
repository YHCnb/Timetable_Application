package com.example.timetable_application.entity

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.room.*
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.lang.reflect.Type

@Parcelize
data class Course(
    var name:String,
    var color:String,
    val time:@RawValue MutableList<CourseTime>,
) : Parcelable
// 课程时间段
data class CourseTime(
    var dayOfWeek: Int=1,
    var timeOfCourse: List<Int> = listOf(1,2),
    var weeks: List<Int> = listOf(1,2,3,4,5),
    var teacher: String= "",
    var position: String= ""
)

@Entity(tableName = "timetable")
@TypeConverters(MapConverter::class)
data class Timetable(
    @PrimaryKey(autoGenerate = false)
    var name:String,
    var startTime:String,
    var curWeek:Int,
    var coursesPerDay:Int=20,
    var weeksOfTerm:Int=20,
    val courseMap:MutableMap<String, Course>
)
//map类型转换器
class MapConverter {
    @TypeConverter
    fun fromMap(map: MutableMap<String?, Course?>?): String {
        val gson = Gson()
        return gson.toJson(map)
    }

    @TypeConverter
    fun toMap(value: String?): MutableMap<String, Course> {
        val gson = Gson()
        val type: Type = object : TypeToken<MutableMap<String?, Course?>?>() {}.getType()
        return gson.fromJson(value, type)
    }
}

