package com.example.timetable_application.entity

import java.sql.Time

data class OneTime(
    val startTiem:Time,
    val endTime:Time
)

data class TimeSchedule(
    val times:MutableList<OneTime>
)