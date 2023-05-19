package com.example.timetable_application.utils

import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.CourseTime
import com.example.timetable_application.entity.TemplateColors
import com.example.timetable_application.entity.Timetable
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun parselWebResponse(message: CourseScheduleResponse): Timetable {
    // 计算curWeek
    val currentDate = LocalDate.now()
    val weeksSinceFirstWeek = ChronoUnit.WEEKS.between(message.firstDay, currentDate).toInt()
    val curWeek= weeksSinceFirstWeek+1
    // 给予课程随机颜色
    val colorList = TemplateColors.colorList
    val selectedColors = mutableSetOf<Int>()

    val weeksOfTerm=message.courseList[0].SKZC!!.length
    val courseMap:MutableMap<String, Course> = mutableMapOf()

    message.courseList.forEach{
        if (it.KCM!=null){
            val name = it.KCM!!.replace(Regex("/"), "|")
            if (!courseMap.contains(name)){
                var randomIndex = (colorList.indices).random() // 随机生成索引
                while (selectedColors.contains(randomIndex)) {
                    randomIndex = (colorList.indices).random()
                }
                selectedColors.add(randomIndex)
                courseMap[name] = Course(name,colorList[randomIndex].substring(1), mutableListOf<CourseTime>())
            }

            val weeks:MutableList<Int> = mutableListOf()
            it.SKZC!!.forEachIndexed { index, c ->
                if (c == '1') {
                    weeks.add(index+1)
                }
            }
            courseMap[name]!!.time.add(CourseTime(it.SKXQ!!-1,(it.KSJC!!..it.JSJC!!).toList(),weeks,it.SKJS,it.JASMC))
        }
    }

    return Timetable(message.term,message.firstDay.toString(),
        curWeek,15,weeksOfTerm,courseMap)
}