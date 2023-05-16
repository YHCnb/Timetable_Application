package com.example.timetable_application.ui.screen.timetable.timetableScreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.CourseTime
import com.example.timetable_application.entity.OneClassTime
import com.example.timetable_application.ui.screen.timetable.TimeScheduleEditor
import com.example.timetable_application.ui.screen.timetable.dialogs.CourseMessageDialog
import java.lang.Long
import java.sql.Time
import java.time.LocalDate

@Composable
fun OneWeekTimetable(navController: NavController, courseMap:MutableMap<String, Course>,
                     currentWeekDates:List<LocalDate>, weekDays:List<String>,
                     currentWeek:Int,coursesPerDay:Int,timeSchedule:MutableList<OneClassTime>){
    val rowHeight = 35.dp//周一到周日的格子的高度
    val monthWidth = 35.dp//最左侧一栏的宽度
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
    var showMessage by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf(CourseTime()) }

    Column(
        modifier = Modifier.padding(top = rowHeight)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val courseBoxHeight = 65.dp
            val weekDayBoxWidth = (maxWidth - monthWidth) / 7

            // 绘制左侧的时间段
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(monthWidth)
                    .clickable(
                        enabled = true
                    ) {
                        navController.navigate("TimeScheduleEditor")
                    }
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
                            text = timeSchedule[i-1].startTime.toString().substring(0,5),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .width(monthWidth)
                        )
                        Text(
                            text = timeSchedule[i-1].endTime.toString().substring(0,5),
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
                                .padding(1.dp)
                                .clickable(enabled = true) {
//                                    navController.currentBackStackEntry?.savedStateHandle?.set(
//                                        "courseList",
//                                        courseMap.values.toList()
//                                    )
//                                    //根据课程名字进入对应的CourseEditor
//                                    navController.navigate("CourseEditor/${course.name}")
                                    title = course.name
                                    message = courseTime
                                    showMessage = true
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(Long.parseLong("FF${course.color}", 16))
                            ),
                            shape = MaterialTheme.shapes.extraSmall,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = course.name+"\n@"+courseTime.position,
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                modifier = Modifier.padding(3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    CourseMessageDialog(showMessage,title,message, onClose = {showMessage=false}){
        navController.currentBackStackEntry?.savedStateHandle?.set(
            "courseList",
            courseMap.values.toList()
        )
        //根据课程名字进入对应的CourseEditor
        navController.navigate("CourseEditor/${title}")
    }
}