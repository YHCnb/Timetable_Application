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
import androidx.compose.ui.unit.dp

@Composable
fun CourseTimePicker(
    coursesPerDay:Int,
    initialDayOfWeek:Int,
    initialStartPeriod:Int,
    initialEndPeriod:Int,
    onTimeSelected: (dayOfWeek: Int, startPeriod: Int, endPeriod: Int) -> Unit,
) {
    val daysOfWeek = listOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
    var selectedDay by remember { mutableStateOf(daysOfWeek[initialDayOfWeek]) }
    var selectedStart by remember { mutableStateOf(initialStartPeriod) }
    var selectedEnd by remember { mutableStateOf(initialEndPeriod) }

    val showDialog = remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { showDialog.value = true }
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color(0xFF5A5A5A),
            modifier = Modifier.size(32.dp)
        )
        Column(
            Modifier
                .padding(start = 24.dp)
                .align(Alignment.CenterStart)
        ) {
            Text(
                text = "${daysOfWeek[initialDayOfWeek]}  第${selectedStart}-${selectedEnd}节",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodyMedium
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
            onDismissRequest = { showDialog.value = false },
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
                TextButton(onClick = { showDialog.value = false }) {
                    Text("取消")
                }
            },
            modifier = Modifier.width(300.dp),
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("周几")
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            itemsIndexed(daysOfWeek) { index, day ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    RadioButton(
                                        selected = (day == selectedDay),
                                        onClick = { selectedDay = day },
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(day)
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("开始")
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            for (i in 1..coursesPerDay){
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        RadioButton(
                                            selected = (i == selectedStart),
                                            onClick = {
                                                updateSelectedStart( i )
                                            },
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("第${i}节")
                                    }
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("结束")
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            for (i in 1..coursesPerDay){
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        RadioButton(
                                            selected = (i == selectedEnd),
                                            onClick = {
                                                updateSelectedEnd( i )
                                            },
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("第${i}节")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}