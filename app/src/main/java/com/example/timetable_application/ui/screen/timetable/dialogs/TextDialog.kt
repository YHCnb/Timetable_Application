package com.example.timetable_application.ui.screen.timetable.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextDialog(
    showDialog:Boolean,
    title:String,
    initialText:String,
    onClose:() -> Unit,
    onTextChanged:(newText: String) -> Unit,
){
    var text by remember { mutableStateOf(initialText) }
    if(showDialog){
        AlertDialog(
            onDismissRequest = { onClose() },
            title = { Text(title) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTextChanged(text)
                        text=initialText
                        onClose()
                    }
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
                        TextField(
                            value = text,
                            onValueChange = {
                                text = it
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        )
    }
}