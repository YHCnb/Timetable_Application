package com.example.timetable_application.utils

import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.CourseTime
import com.example.timetable_application.entity.TemplateColors
import com.example.timetable_application.entity.Timetable
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun parseICS(text: String?): Timetable {
    val courseTimeRegex1 = "(\\d+)-(\\d+)周\\s+(\\S+)\\s+(\\d+)-(\\d+)节\\s+(.+)?".toRegex()
    val courseTimeRegex2 = "(\\d+)周\\s+(\\S+)\\s+(\\d+)-(\\d+)节\\s+(.+)?".toRegex()
    val pattern1 = ("(\\d+)周").toRegex()
    val pattern2 = ("(\\d+)-(\\d+)周").toRegex()

    val colorList = TemplateColors.colorList
    val selectedColors = mutableSetOf<Int>()
    var timetableName = ""
    var start = ""
    var curWeek = 1
    var weeksOfTerm=0

    val courseMap:MutableMap<String, Course> = mutableMapOf()
    var isFirst = true
    text?.split("BEGIN:VEVENT")?.forEach { event ->
        if (isFirst){
            isFirst = false
        }else{
            val summary = event.substringAfter("SUMMARY:").substringBefore("\n").replace(Regex("/"), "|")
            if (! courseMap.contains(summary)){
                val time :MutableList<CourseTime> = mutableListOf()

                val description  = event.substringAfter("DESCRIPTION:").substringBefore("DTSTART:")
                val values = description.split(" | ")
                val teacher = values[0]
                //用于存储不连续时间段
                val cunList:MutableList<Int> = mutableListOf()

                values[1].split(",").forEach {
                    if (it.last()=='周'){
                        var courseTimeMatch = pattern2.matchEntire(it)
                        if (courseTimeMatch != null) {
                            val (startWeek, endWeek) = courseTimeMatch.destructured
                            for (w in startWeek.toInt()..endWeek.toInt()){
                                cunList.add(w)
                            }
                        }else{
                            courseTimeMatch = pattern1.matchEntire(it)
                            if (courseTimeMatch !=null){
                                val (startWeek) = courseTimeMatch.destructured
                                cunList.add(startWeek.toInt())
                            }
                        }
                    }else{
                        val s = it.replace(Regex("\n\\s*"), "")
                        var courseTimeMatch = courseTimeRegex1.matchEntire(s)
                        if (courseTimeMatch != null) {
                            val (startWeek, endWeek, dayOfWeek, startTime,endTime, position) = courseTimeMatch.destructured
                            val weeks:MutableList<Int> = (startWeek.toInt()..endWeek.toInt()).toMutableList()
                            val timeOfCourse = (startTime.toInt()..endTime.toInt()).toList()
                            val dayOfWeekIndex = when (dayOfWeek) {
                                "星期一" -> 0
                                "星期二" -> 1
                                "星期三" -> 2
                                "星期四" -> 3
                                "星期五" -> 4
                                "星期六" -> 5
                                "星期日" -> 6
                                else -> throw IllegalArgumentException("Invalid day of week: $dayOfWeek")
                            }
                            if(endWeek.toInt()>weeksOfTerm){
                                weeksOfTerm = endWeek.toInt()
                            }
                            if(start==""){ //计算
                                val dtStart = event.substringAfter("DTSTART:").substringBefore("\n")
                                val year = dtStart.substring(0, 4)
                                val month = dtStart.substring(4, 6)
                                val day = dtStart.substring(6, 8)

                                timetableName = if (month.toInt()<8){
                                    "${year.toInt()-1}-${year.toInt()}-2"
                                }else "${year.toInt()}-${year.toInt()+1}-1"

                                val knownDate = LocalDate.of(year.toInt(), month.toInt(), day.toInt())
                                val knownDayOfWeek = knownDate.dayOfWeek
                                val daysToMinus = (startWeek.toInt()-1) * 7 + knownDayOfWeek.value -1
                                val firstDayOfFirstWeek = knownDate.minusDays(daysToMinus.toLong())

                                start = firstDayOfFirstWeek.toString()

                                val currentDate = LocalDate.now()
                                val weeksSinceFirstWeek = ChronoUnit.WEEKS.between(firstDayOfFirstWeek, currentDate).toInt()

                                curWeek= weeksSinceFirstWeek + 1 // 加上第一周
                            }

                            for (w in cunList){
                                weeks.add(w)
                            }
                            cunList.clear()
                            val courseTime = CourseTime(dayOfWeekIndex, timeOfCourse, weeks, teacher, position)
                            println(courseTime)
                            time.add(courseTime)
                        }else{
                            courseTimeMatch = courseTimeRegex2.matchEntire(s)
                            val (startWeek, dayOfWeek, startTime,endTime, position) = courseTimeMatch!!.destructured
                            val weeks:MutableList<Int> = (startWeek.toInt()..startWeek.toInt()).toMutableList()
                            val timeOfCourse = (startTime.toInt()..endTime.toInt()).toList()
                            val dayOfWeekIndex = when (dayOfWeek) {
                                "星期一" -> 0
                                "星期二" -> 1
                                "星期三" -> 2
                                "星期四" -> 3
                                "星期五" -> 4
                                "星期六" -> 5
                                "星期日" -> 6
                                else -> throw IllegalArgumentException("Invalid day of week: $dayOfWeek")
                            }
                            if(start==""){ //计算
                                val dtStart = event.substringAfter("DTSTART:").substringBefore("\n")
                                val year = dtStart.substring(0, 4)
                                val month = dtStart.substring(4, 6)
                                val day = dtStart.substring(6, 8)

                                timetableName = if (month.toInt()<8){
                                    "${year.toInt()-1}-${year.toInt()}-2"
                                }else "${year.toInt()}-${year.toInt()+1}-1"

                                val knownDate = LocalDate.of(year.toInt(), month.toInt(), day.toInt())
                                val knownDayOfWeek = knownDate.dayOfWeek
                                val daysToMinus = (startWeek.toInt()-1) * 7 + knownDayOfWeek.value -1
                                val firstDayOfFirstWeek = knownDate.minusDays(daysToMinus.toLong())

                                start = firstDayOfFirstWeek.toString()

                                val currentDate = LocalDate.now()
                                val weeksSinceFirstWeek = ChronoUnit.WEEKS.between(firstDayOfFirstWeek, currentDate).toInt()

                                curWeek= weeksSinceFirstWeek + 1 // 加上第一周
                            }

                            for (w in cunList){
                                weeks.add(w)
                            }
                            cunList.clear()
                            val courseTime = CourseTime(dayOfWeekIndex, timeOfCourse, weeks, teacher, position)
                            println(courseTime)
                            time.add(courseTime)
                        }
                    }
                }
                var randomIndex = (colorList.indices).random() // 随机生成索引
                while (selectedColors.contains(randomIndex)) {
                    randomIndex = (colorList.indices).random()
                }
                selectedColors.add(randomIndex)
                courseMap[summary] = Course(summary,colorList[randomIndex].substring(1), time)
            }
        }
    }
    return Timetable(timetableName,start,curWeek,15,weeksOfTerm,courseMap)
}