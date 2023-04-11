package com.example.timetable_application.ui.screen.timetable.courseEditorScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.timetable_application.entity.CourseTime
import com.example.timetable_application.ui.screen.timetable.pickers.CourseTimePicker
import com.example.timetable_application.ui.screen.timetable.pickers.TextPicker
import com.example.timetable_application.ui.screen.timetable.pickers.WeeksPicker

@Composable
fun CourseTimeEditor(time: CourseTime, weeksOfTerm:Int, coursesPerDay:Int, onTimeChanged: (CourseTime) -> Unit, onDeleteTime: () -> Unit) {
    var selectedStartPeriod = time.timeOfCourse.first()
    var selectedEndPeriod = time.timeOfCourse.last()
    var selectedDayOfWeek = time.dayOfWeek
    var selectedWeeks = time.weeks
    var teacherState = time.teacher
    var positionState = time.position

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "时间段",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            onClick = onDeleteTime
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "删除时间段"
            )
        }
    }
    WeeksPicker(weeksOfTerm = weeksOfTerm, initialWeeks = selectedWeeks, onWeeksChanged = {
        selectedWeeks=it
        onTimeChanged(time.copy(weeks = selectedWeeks))
    })
    TextPicker(icon = Icons.Default.Email, initialText = teacherState, title = "教师名字", nullText = "授课老师(可不填)"){
        teacherState = it
        onTimeChanged(time.copy(teacher = teacherState))
    }
    TextPicker(icon = Icons.Default.Home, initialText = positionState, title = "上课地点", nullText = "上课地点(可不填)"){
        positionState = it
        onTimeChanged(time.copy(position = positionState))
    }
    CourseTimePicker(
        coursesPerDay = coursesPerDay,
        initialDayOfWeek = selectedDayOfWeek,
        initialStartPeriod = selectedStartPeriod,
        initialEndPeriod = selectedEndPeriod,
        onTimeSelected ={dayOfWeek, startPeriod, endPeriod->
            selectedDayOfWeek = dayOfWeek
            selectedStartPeriod = startPeriod
            selectedEndPeriod = endPeriod
            onTimeChanged(
                time.copy(dayOfWeek = selectedDayOfWeek,timeOfCourse = (selectedStartPeriod..selectedEndPeriod).toList())
            )
        }
    )
}