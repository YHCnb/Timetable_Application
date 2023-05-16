package com.example.timetable_application.ui.screen.timetable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.timetable_application.db.DbHelper
import com.example.timetable_application.entity.OneClassTime
import com.example.timetable_application.entity.TimetableViewModel
import com.example.timetable_application.ui.screen.timetable.dialogs.SaveOrLeaveDialog
import com.example.timetable_application.ui.screen.timetable.dialogs.TimeScheduleDialog
import com.example.timetable_application.ui.theme.Orange30

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeScheduleEditor(navController: NavController,vm: TimetableViewModel){
    var times = DbHelper.creatTimeSchedule()
    var showDialog by remember { mutableStateOf(false) }
    var initialOneClassTime by remember { mutableStateOf(times[0]) }
    var onChangeIndex = -1

    Scaffold(
        topBar = {
            TimeScheduleTopAppBar(onBackPressed = { navController.popBackStack() }) {

            }
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ){
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    times.forEachIndexed { index, time ->
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(1.dp),
                            verticalAlignment = Alignment.CenterVertically  ,
                        ) {
                            Spacer(modifier = Modifier.width(25.dp))
                            Text(
                                text= "第${index + 1}节",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(
                                onClick = {
                                    onChangeIndex= index
                                    initialOneClassTime = time
                                    showDialog =true
                                },
                                border = BorderStroke(2.dp, Orange30),
                            ) {
                                Text(time.toString())
                            }
                        }
                    }
                }
                TimeScheduleDialog(
                    showDialog = showDialog,
                    initialOneClassTime = initialOneClassTime,
                    onDismiss = { showDialog=false }
                ) {
                    times[onChangeIndex] = OneClassTime(onChangeIndex,it.first,it.second)
                    showDialog = false
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeScheduleTopAppBar(onBackPressed: () -> Unit,onConfirm: () -> Unit) {
    //开始处于隐藏状态
    val openDialog = remember { mutableStateOf(false) }
    SaveOrLeaveDialog(openDialog = openDialog, onConfirm = {onConfirm()},onDismiss = {onBackPressed()})

    TopAppBar(title = { Text(text = "编辑时间表", fontWeight = FontWeight.ExtraBold) },
        navigationIcon = {
            IconButton(
                onClick = {
                    openDialog.value=true
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                )
            ) {
                Icon(imageVector  = Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}