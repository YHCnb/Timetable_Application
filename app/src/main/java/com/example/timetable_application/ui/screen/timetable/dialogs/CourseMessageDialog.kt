package com.example.timetable_application.ui.screen.timetable.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.timetable_application.R
import com.example.timetable_application.entity.CourseTime
import com.example.timetable_application.entity.Week
import com.example.timetable_application.ui.screen.timetable.pickers.selectedWeeksText

@Composable
fun CourseMessageDialog(
    showDialog:Boolean,
    title:String,
    message:CourseTime,
    onClose:() -> Unit,
    onConfirm:() -> Unit,
){
    val daysOfWeek = Week.dayOfWeek
    if(showDialog){
        AlertDialog(
            onDismissRequest = { onClose() },
            title = { Text(title) },
            confirmButton = {
                TextButton(
                    onClick = { onConfirm() }
                ) {
                    Text(
                        text = "编辑",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            },
            dismissButton = {

            },
            modifier = Modifier.width(300.dp),
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable {  }
                                .padding(0.dp,10.dp)
                        ) {
                            Row(
                                Modifier.fillMaxHeight()
                                    .fillMaxWidth()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.plan),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(
                                    text = selectedWeeksText(message.weeks),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable {  }
                                .padding(0.dp,10.dp)
                        ) {
                            Row(
                                Modifier.fillMaxHeight()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.time),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(
                                    text = "${daysOfWeek[message.dayOfWeek]}  第${message.timeOfCourse.first()}-${message.timeOfCourse.last()}节",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable {  }
                                .padding(0.dp,10.dp)
                        ) {
                            Row(
                                Modifier.fillMaxHeight()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.edit_name),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(
                                    text = if(message.teacher=="") "未填写" else message.teacher!!,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable {  }
                                .padding(0.dp,10.dp)
                        ) {
                            Row(
                                Modifier.fillMaxHeight()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.building_three),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(
                                    text = if(message.position=="") "未填写" else message.position!!,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}