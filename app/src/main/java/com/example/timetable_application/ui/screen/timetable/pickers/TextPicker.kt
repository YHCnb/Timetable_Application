package com.example.timetable_application.ui.screen.timetable.pickers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.timetable_application.R
import com.example.timetable_application.ui.screen.timetable.dialogs.TextDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextPicker(
    iconPainter: Painter,
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
            .height(50.dp)
            .clickable { showDialog.value = true }
            .padding(10.dp)
    ) {
        Row(
            Modifier.fillMaxHeight()
        ) {
            Icon(
                painter = iconPainter,
                contentDescription = null,
//                modifier = Modifier.fillMaxSize(),
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = if(text=="") nullText else text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
    TextDialog(
        showDialog = showDialog.value,
        title = title,
        initialText = initialText,
        onClose = { showDialog.value=false },
        onTextChanged = {
            text=it
            onTextChanged(it)
        }
    )
}