package com.example.timetable_application.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chargemap.compose.numberpicker.ListItemPicker
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.common.util.CollectionUtils.listOf


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OtherCompose(navController: NavController){
    val list = listOf(0,1,2,3,4,5,6,7,8,9,10)
    var pickerValue by remember { mutableStateOf(list[0]) }
    val showDialog = remember { mutableStateOf(false ) }

    Button(onClick = {
        showDialog.value =true
    }) {
        Text(text = pickerValue.toString())
    }

    if(showDialog.value){
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("选择时间") },
            confirmButton = {
                TextButton(
                    onClick = {
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