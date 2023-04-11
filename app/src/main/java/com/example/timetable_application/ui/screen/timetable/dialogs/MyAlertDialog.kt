package com.example.timetable_application.ui.screen.timetable.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyAlertDialog(
    showDialog:Boolean,
    title:String,
    message:String,
    onClose:() -> Unit,
    onConfirm:() -> Unit,
){
    if(showDialog){
        AlertDialog(
            onDismissRequest = { onClose() },
            title = { Text(title) },
            confirmButton = {
                TextButton(
                    onClick = { onConfirm() }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { onClose() }) {
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(message)
                    }
                }
            }
        )
    }
}