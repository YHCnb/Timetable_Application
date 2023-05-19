package com.example.timetable_application.ui.screen.timetable.pickers

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.timetable_application.ui.screen.timetable.dialogs.NumberDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerNumberPicker(
    title:String, list: List<Any>, initialIndex:Int,
    onConfirm:(index: Int)-> Unit, onClose: (() -> Unit)? = null, onDismiss: (() -> Unit)? = null){
    val showNumberDialog = remember { mutableStateOf(false) }

    NavigationDrawerItem(
        label = {
            Text(text = title)
        },
        selected = false,
        onClick = {
            onClose?.invoke()
            showNumberDialog.value=true
        }
    )
    NumberDialog(
        title = title,
        show = showNumberDialog.value,
        list = list,
        initialIndex = initialIndex,
        onDismiss = {
            showNumberDialog.value=false
            onDismiss?.invoke()
        },
    ) {
        showNumberDialog.value=false
        onConfirm(it)
    }
}