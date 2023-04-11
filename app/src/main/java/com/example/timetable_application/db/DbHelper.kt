package com.example.timetable_application.db

import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.CourseTime
import com.example.timetable_application.entity.Timetable
import com.google.android.gms.common.util.CollectionUtils.listOf
import java.util.*
import kotlin.random.Random

object DbHelper{
    fun creatExampleTimetable(): Timetable {
        val courseTime1 = CourseTime(1, listOf(1, 2, 3), listOf(1,2),"张老师", "教室101")
        val courseTime2 = CourseTime(3, listOf(4, 5), listOf(1,2,3),"李老师", "教室202")
        val course1 = Course("数学", "0000FF", mutableListOf(courseTime1))
        val course2 = Course("英语", "0000FF", mutableListOf(courseTime2))
        val courseMap = mutableMapOf("数学" to course1, "英语" to course2)
        val timetable = Timetable("我的课表", "2023-04-01", 1, 20, 20, courseMap)
        return timetable
    }

    //新建课表
    fun creatNewTimetable(name: String): Timetable {
        val courseMap = mutableMapOf<String,Course>()
        return Timetable(name, "2023-01-01", 1, 20, 20, courseMap)
    }
    fun creatExampleCourse(): Course {
        val courseTime1 = CourseTime(1, listOf(1, 2, 3), listOf(1,2))
        val course = Course("", "0000FF", mutableListOf(courseTime1))
        return course
    }

}