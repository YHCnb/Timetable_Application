package com.example.timetable_application.ui.screen.timetable.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.example.timetable_application.entity.OneClassTime
import com.example.timetable_application.entity.OneTime

@Composable
fun TimeScheduleDialog(
    showDialog:Boolean,
    initialOneClassTime:OneClassTime,
    onDismiss:()->Unit,
    onConfirm: (Pair<OneTime,OneTime>) -> Unit,
){
    var startHour by remember { mutableStateOf(initialOneClassTime.startTime.hour) }
    var startMinute by remember { mutableStateOf(initialOneClassTime.startTime.minute) }
    var endHour by remember { mutableStateOf(initialOneClassTime.endTime.hour) }
    var endMinute by remember { mutableStateOf(initialOneClassTime.endTime.minute) }

    fun plusMinutes(){
        if(startMinute+45>=60){
            if(startHour==23){
                endMinute=59
            }else{
                endHour=startHour+1
                endMinute=startMinute+45-60
            }
        }else{
            endHour=startHour
            endMinute=startMinute+45
        }
    }
    fun updateStartHour(newHour: Int){
        startHour=newHour
        plusMinutes()
    }
    fun updateStartMinute(newMinute: Int){
        startMinute=newMinute
        plusMinutes()
    }
    fun updateEndHour(newHour: Int){//只有满足约束才更新
        if(newHour>startHour || (newHour==startHour&&startMinute<endMinute)){
            endHour=newHour
        }
    }
    fun updateEndMinute(newMinute: Int){//只有满足约束才更新
        if(endHour>startHour || (endHour==startHour&&startMinute<newMinute)){
            endMinute=newMinute
        }
    }

    if(showDialog){
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = { Text("选择时间") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(
                            Pair(OneTime(startHour,startMinute), OneTime(endHour,endMinute))
                        )
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
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
                        value = startHour.toString().padStart(2, '0'),
                        dividersColor = Color.Black,
                        list = (6..23).map { it.toString().padStart(2, '0') },
                        onValueChange = {
                            updateStartHour(it.toInt())
                        }
                    )
                    ListItemPicker(
                        textStyle = MaterialTheme.typography.titleMedium,
                        value = startMinute.toString().padStart(2, '0'),
                        dividersColor = Color.Black,
                        list = (0..59).map { it.toString().padStart(2, '0') },
                        onValueChange = {
                            updateStartMinute(it.toInt())
                        }
                    )
                    ListItemPicker(
                        textStyle = MaterialTheme.typography.titleMedium,
                        value = endHour.toString().padStart(2, '0'),
                        dividersColor = Color.Black,
                        list = (6..23).map { it.toString().padStart(2, '0') },
                        onValueChange = {
                            updateEndHour(it.toInt())
                        }
                    )
                    ListItemPicker(
                        textStyle = MaterialTheme.typography.titleMedium,
                        value = endMinute.toString().padStart(2, '0'),
                        dividersColor = Color.Black,
                        list = (0..59).map { it.toString().padStart(2, '0') },
                        onValueChange = {
                            updateEndMinute(it.toInt())
                        }
                    )
                }
            }
        )
    }
}