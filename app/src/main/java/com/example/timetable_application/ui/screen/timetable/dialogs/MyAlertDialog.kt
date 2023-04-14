package com.example.timetable_application.ui.screen.timetable.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
                    Text(
                        text = "确定",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
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