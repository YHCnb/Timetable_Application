package com.example.timetable_application.ui.screen.timetable.timetableScreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.TimeSchedule
import java.lang.Long
import java.sql.Time
import java.time.LocalDate

@Composable
fun OneWeekTimetable(navController: NavController, courseMap:MutableMap<String, Course>,
                     currentWeekDates:List<LocalDate>, weekDays:List<String>, currentWeek:Int,coursesPerDay:Int,timeSchedule:List<Pair<Time, Time>>){
    val rowHeight = 35.dp//周一到周日的格子的高度
    val monthWidth = 40.dp//最左侧一栏的宽度
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(monthWidth)
        ) {
            Text(
                text =currentWeekDates[0].month.value.toString()+"月",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
        // 横向布局周一到周日的格子
        for (i in 0..6) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = weekDays[i],
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = currentWeekDates[i].month.value.toString()+"/"+currentWeekDates[i].dayOfMonth.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
    // 课表主体部分
    Column(
        modifier = Modifier.padding(top = rowHeight)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val courseBoxHeight = 60.dp
            val weekDayBoxWidth = (maxWidth - monthWidth) / 7

            // 绘制左侧的时间段
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(monthWidth)
            ) {
                for (i in 1..coursesPerDay) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .height(courseBoxHeight)
                            .width(monthWidth)
                    ) {
                        Text(
                            text = "$i",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = timeSchedule[i-1].first.toString().substring(0,5),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .width(monthWidth)
                        )
                        Text(
                            text = timeSchedule[i-1].second.toString().substring(0,5),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .width(monthWidth)
                        )
                    }
                }
            }

            // 绘制课程矩形
            courseMap.values.forEach() { course ->
                course.time.forEach(){courseTime->
                    if (courseTime.weeks.contains(currentWeek)) {//如果当前周有此课，就显示
                        val courseWeekDay = courseTime.dayOfWeek
                        val courseStartSection = courseTime.timeOfCourse.first()
                        val courseEndSection = courseTime.timeOfCourse.last()

                        val courseStartY = (courseStartSection - 1) * courseBoxHeight
                        val courseEndY = courseEndSection * courseBoxHeight
                        val courseBoxLeft = monthWidth + (courseWeekDay) * weekDayBoxWidth
                        Card(
                            modifier = Modifier
                                .width(weekDayBoxWidth)
                                .height(courseEndY - courseStartY)
                                .offset(x = courseBoxLeft, y = courseStartY)
                                .clickable(enabled = true) {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "courseList",
                                        courseMap.values.toList()
                                    )
                                    //根据课程名字进入对应的CourseEditor
                                    navController.navigate("CourseEditor/${course.name}")
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(Long.parseLong("FF${course.color}", 16))
                            ),
                            shape = MaterialTheme.shapes.extraSmall,
//                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary)
                        ) {
                            Text(
                                text = course.name,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
//                        Box(
//                            modifier = Modifier
//                                .width(weekDayBoxWidth)
//                                .height(courseEndY - courseStartY)
//                                .offset(x = courseBoxLeft, y = courseStartY)
//                                .background(Color(Long.parseLong("FF${course.color}", 16)))
//                                .clickable(enabled = true) {
//                                    navController.currentBackStackEntry?.savedStateHandle?.set(
//                                        "courseList",
//                                        courseMap.values.toList()
//                                    )
//                                    //根据课程名字进入对应的CourseEditor
//                                    navController.navigate("CourseEditor/${course.name}")
//                                }
//                        ) {
//                            Text(
//                                text = course.name,
//                                textAlign = TextAlign.Center,
//                                style = MaterialTheme.typography.titleMedium,
//                                color = Color.White,
//                                modifier = Modifier.padding(8.dp)
//                            )
//                        }
                    }
                }
            }
        }
    }
}