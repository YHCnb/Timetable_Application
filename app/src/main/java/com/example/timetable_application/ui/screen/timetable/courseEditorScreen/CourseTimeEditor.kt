package com.example.timetable_application.ui.screen.timetable.courseEditorScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.timetable_application.R
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
            modifier = Modifier.padding(10.dp),
            text = "时间段",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        IconButton(
            onClick = onDeleteTime
        ) {
            Icon(
                painter = painterResource(id = R.drawable.delete_key),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
    WeeksPicker(weeksOfTerm = weeksOfTerm, initialWeeks = selectedWeeks, onWeeksChanged = {
        selectedWeeks=it
        onTimeChanged(time.copy(weeks = selectedWeeks))
    })
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
    TextPicker(iconPainter = painterResource(id = R.drawable.edit_name), initialText = teacherState, title = "教师名字", nullText = "授课老师(可不填)"){
        teacherState = it
        onTimeChanged(time.copy(teacher = teacherState))
    }
    TextPicker(iconPainter = painterResource(id = R.drawable.building_three), initialText = positionState, title = "上课地点", nullText = "上课地点(可不填)"){
        positionState = it
        onTimeChanged(time.copy(position = positionState))
    }
}