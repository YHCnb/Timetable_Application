package com.example.timetable_application.ui.screen.timetable.pickers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.*

@Composable
fun ColorPicker(
    initialColor: String,
    onColorChanged: (String) -> Unit,
) {
    var selectedColor by remember { mutableStateOf(initialColor) }
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
            tint = Color(java.lang.Long.parseLong("FF$initialColor", 16)),
            modifier = Modifier.size(32.dp)
        )
        Column(
            Modifier
                .padding(start = 24.dp)
                .align(Alignment.CenterStart)
        ) {
            Text(
                text = "点此更改颜色",
                color = Color(java.lang.Long.parseLong("FF$initialColor", 16)),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    //这里使用了github开源的colorpicker-compose
    val controller = rememberColorPickerController()
    if (showDialog.value){
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onColorChanged(selectedColor)
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
                        HsvColorPicker(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(5.dp),
                            controller = controller,
                            onColorChanged = { colorEnvelope: ColorEnvelope ->
                                selectedColor = colorEnvelope.hexCode.substring(2)
                            }
                        )
                        BrightnessSlider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .height(35.dp),
                            controller = controller,
                        )
                        AlphaTile(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            controller = controller
                        )
                    }
                }
            }
        )
    }
}