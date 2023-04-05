package com.example.timetable_application.ui.screen.timetable.courseEditorScreen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.DialogProperties

@Composable
fun SaveOrLeaveDialog(
    openDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    //value改为false后又会执行这一条，然后AlertDialog就会消失
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            text = { Text("是否保存当前编辑？") },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()
                        openDialog.value = false
                    }
                ) {
                    Text("保存")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismiss()
                        openDialog.value = false
                    }
                ) {
                    Text("离开")
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }
}