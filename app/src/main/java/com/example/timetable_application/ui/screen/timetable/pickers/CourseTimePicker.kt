package com.example.timetable_application.ui.screen.timetable.pickers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.example.timetable_application.R
import com.example.timetable_application.entity.Week

@Composable
fun CourseTimePicker(
    coursesPerDay:Int,
    initialDayOfWeek:Int,
    initialStartPeriod:Int,
    initialEndPeriod:Int,
    onTimeSelected: (dayOfWeek: Int, startPeriod: Int, endPeriod: Int) -> Unit,
) {
    val daysOfWeek = Week().dayOfWeek
    val periodList = (1..coursesPerDay).toList()
    var selectedDay by remember { mutableStateOf(daysOfWeek[initialDayOfWeek]) }
    var selectedStart by remember { mutableStateOf(initialStartPeriod) }
    var selectedEnd by remember { mutableStateOf(initialEndPeriod) }

    val showDialog = remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { showDialog.value = true }
            .padding(10.dp)
    ) {
        Row(
            Modifier.fillMaxHeight()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.time),
                contentDescription = null,
//                modifier = Modifier.fillMaxSize(),
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "${daysOfWeek[initialDayOfWeek]}  第${initialStartPeriod}-${initialEndPeriod}节",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
    fun updateSelectedStart(newStart: Int){//只有满足约束才更新
        if (selectedEnd >= newStart) {
            selectedStart = newStart
        }else{
            selectedStart = newStart
            selectedEnd = newStart
        }
    }
    fun updateSelectedEnd(newEnd: Int) {
        if (newEnd >= selectedStart) {
            selectedEnd = newEnd
        }
    }
    if(showDialog.value){
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
                selectedDay = daysOfWeek[initialDayOfWeek]
                selectedStart = initialStartPeriod
                selectedEnd = initialEndPeriod
            },
            title = { Text("选择时间") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeSelected(
                            daysOfWeek.indexOf(selectedDay),
                            selectedStart,
                            selectedEnd
                        )
                        showDialog.value = false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    selectedDay = daysOfWeek[initialDayOfWeek]
                    selectedStart = initialStartPeriod
                    selectedEnd = initialEndPeriod
                }) {
                    Text("取消")
                }
            },
            modifier = Modifier.width(300.dp),
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    ListItemPicker(
                        textStyle = MaterialTheme.typography.titleMedium,
                        value = selectedDay,
                        dividersColor = Color.Black,
                        list = daysOfWeek,
                        onValueChange = {
                            selectedDay = it
                        }
                    )
                    ListItemPicker(
                        textStyle = MaterialTheme.typography.titleMedium,
                        value = selectedStart,
                        dividersColor = Color.Black,
                        list = periodList,
                        onValueChange = {
                            updateSelectedStart(it)
                        }
                    )
                    ListItemPicker(
                        textStyle = MaterialTheme.typography.titleMedium,
                        value = selectedEnd,
                        dividersColor = Color.Black,
                        list = periodList,
                        onValueChange = {
                            updateSelectedEnd(it)
                        }
                    )
                }
            }
        )
    }
}