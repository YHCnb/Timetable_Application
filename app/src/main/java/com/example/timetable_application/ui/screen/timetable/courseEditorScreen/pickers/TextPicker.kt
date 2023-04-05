package com.example.timetable_application.ui.screen.timetable.courseEditorScreen.pickers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextPicker(
    icon: ImageVector,
    initialText:String,
    title:String,
    nullText:String,//text为空时显示的内容
    onTextChanged:(newText: String) -> Unit,
){
    var text by remember { mutableStateOf(initialText) }
    val showDialog = remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { showDialog.value = true }
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
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
                text = if(text=="") nullText else text,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    if(showDialog.value){
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(title) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTextChanged(text)
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