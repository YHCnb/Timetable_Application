package com.example.timetable_application.ui.screen.timetable.pickers

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNumberPicker(title:String ,list: List<Any>,initialIndex:Int,
                   onConfirm:(index: Int)-> Unit, onClose:()-> Unit, onDismiss:()-> Unit){
    val showNumberPicker = remember { mutableStateOf(false) }
    var pickerValue by remember { mutableStateOf(list[initialIndex]) }

    NavigationDrawerItem(
        label = {
            Text(text = title)
        },
        selected = false,
        onClick = {
            onClose()
            showNumberPicker.value=true
        }
    )

    if(showNumberPicker.value){
        AlertDialog(
            onDismissRequest = {
                showNumberPicker.value = false
                onDismiss()
            },
            title = { Text("$title: $pickerValue") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(list.indexOf(pickerValue)+1)
                        showNumberPicker.value=false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showNumberPicker.value=false
                    onDismiss()
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