package com.example.timetable_application.db

import com.example.timetable_application.entity.*
import com.google.android.gms.common.util.CollectionUtils.listOf

object DbHelper{
    fun creatExampleTimetable(): Timetable {
        val courseTime1 = CourseTime(0,listOf(6,7),listOf(1, 2, 3,4,5,6,7,8),"金旭亮", "良乡校区综教A504")
        val courseTime2 = CourseTime(3, listOf(1,2),listOf(1, 2, 3,4,5,6,7,8),"金旭亮", "良乡校区综教A504")
        val course1 = Course("Android技术开发基础", "E35E69", mutableListOf(courseTime1,courseTime2))
        val courseMap = mutableMapOf("Android技术开发基础" to course1)
        val timetable = Timetable("我的课表", "2023-02-20", 1, 20, 20, courseMap)
        return timetable
    }

    //新建课表
    fun creatNewTimetable(name: String): Timetable {
        val courseMap = mutableMapOf<String,Course>()
        return Timetable(name, "2023-01-01", 1, 20, 20, courseMap)
    }
    fun creatExampleCourse(): Course {
        val courseTime1 = CourseTime(1, listOf(1, 2, 3), listOf(1, 2))
        return Course("", TemplateColors().colorList.random().substring(1), mutableListOf(courseTime1))
    }
    fun creatTimeSchedule(): MutableList<OneClassTime> {
        val times = mutableListOf(
            OneClassTime(0,OneTime(8, 0), OneTime(8, 45)),
            OneClassTime(1,OneTime(8, 50), OneTime(9, 35)),
            OneClassTime(2,OneTime(9, 55), OneTime(10, 40)),
            OneClassTime(3,OneTime(10, 45), OneTime(11, 30)),
            OneClassTime(4,OneTime(11, 35), OneTime(12, 20)),
            OneClassTime(5,OneTime(13, 20), OneTime(14, 5)),
            OneClassTime(6,OneTime(14, 10), OneTime(14, 55)),
            OneClassTime(7,OneTime(15, 15), OneTime(16, 0)),
            OneClassTime(8,OneTime(16, 5), OneTime(16, 50)),
            OneClassTime(9,OneTime(16, 55), OneTime(17, 40)),
            OneClassTime(10,OneTime(18, 30), OneTime(19, 15)),
            OneClassTime(11,OneTime(19, 20), OneTime(20,5)),
            OneClassTime(12,OneTime(20,10), OneTime(20,55)),
            OneClassTime(13,OneTime(21,45), OneTime(21,50)),
            OneClassTime(14,OneTime(21, 55), OneTime(22, 0)),
            OneClassTime(15,OneTime(22, 5), OneTime(22, 10)),
            OneClassTime(16,OneTime(22, 15), OneTime(22, 20)),
            OneClassTime(17,OneTime(22, 25), OneTime(22, 30)),
            OneClassTime(18,OneTime(22, 35), OneTime(22, 40)),
            OneClassTime(19,OneTime(22, 45), OneTime(22, 50)),
            OneClassTime(20,OneTime(22, 45), OneTime(22, 50)),
            OneClassTime(21,OneTime(22, 45), OneTime(22, 50)),
            OneClassTime(22,OneTime(22, 45), OneTime(22, 50)),
            OneClassTime(23,OneTime(22, 45), OneTime(22, 50)),
            OneClassTime(24,OneTime(22, 45), OneTime(22, 50)),
//            OneClassTime(25,OneTime(22, 45), OneTime(22, 50))
        )
        return times
    }
}