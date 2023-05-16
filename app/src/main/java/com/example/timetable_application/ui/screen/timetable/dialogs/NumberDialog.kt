package com.example.timetable_application.ui.screen.timetable.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker

@Composable
fun NumberDialog(title:String,show:Boolean,list: List<Any>,initialIndex:Int,
                 onDismiss: () -> Unit,onConfirm:(num: Int)-> Unit){
    var pickerValue by remember { mutableStateOf(list[initialIndex]) }
    if(show){
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                pickerValue=list[initialIndex]//恢复初始值
            },
            title = { Text("$title: $pickerValue") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(list.indexOf(pickerValue)+1)
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                    pickerValue=list[initialIndex]//恢复初始值
                }) {
                    Text("取消")
                }
            },
            modifier = Modifier.width(300.dp),
            text = {
                ListItemPicker(
                    modifier = Modifier.fillMaxWidth(),
                    value = pickerValue,
                    dividersColor = Color.Black,
                    list = list,
                    onValueChange = {
                        pickerValue = it
                    }
                )
            }
        )
    }
}