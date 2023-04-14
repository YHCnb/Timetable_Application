package com.example.timetable_application.ui.screen.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.timetable_application.entity.OneTime
import com.example.timetable_application.entity.TimetableViewModel
import com.example.timetable_application.ui.screen.timetable.dialogs.SaveOrLeaveDialog
import java.sql.Time

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeScheduleEditor(navController: NavController,vm: TimetableViewModel){
    val times = listOf(
        OneTime(Time(9,0,0), Time(9,0,0)),
        OneTime(Time(10,0,0),Time(11,0,0)),
        OneTime(Time(11,0,0),Time(12,0,0)),
        OneTime(Time(12,0,0),Time(13,0,0)),
        OneTime(Time(13,0,0),Time(14,0,0)),
        OneTime(Time(14,0,0),Time(15,0,0)),
        OneTime(Time(15,0,0),Time(16,0,0)),
        OneTime(Time(16,0,0),Time(17,0,0)),
        OneTime(Time(17,0,0),Time(18,0,0)),
        OneTime(Time(17,0,0),Time(19,0,0)),
        OneTime(Time(19,0,0),Time(20,0,0)),
        OneTime(Time(20,0,0),Time(21,0,0)),
        OneTime(Time(21,0,0),Time(22,0,0)),
        OneTime(Time(22,0,0),Time(23,0,0)),
    )


    Scaffold(
        topBar = {
            TimeScheduleTopAppBar(onBackPressed = { navController.popBackStack() }) {

            }
        }
    ) {paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .background(Color.LightGray)
        ) {
            Column {
                times.forEachIndexed { index, time ->
                    Row {
                        Text("第${index + 1}节")
                        Spacer(modifier = Modifier.weight(1f))
                        Button(onClick = { /*TODO*/ }) {
                            Text(time.startTiem.toString())
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text(time.endTime.toString())
                        }
                    }
                }
            }
        }
    }

//    val timeScheduleState = UseCaseState(
//        embedded = false,
//    )
//    DateTimeDialog(
//        state = timeScheduleState,
//        selection = DateTimeSelection.Time {date->
//            println(date)
//        }
//    )
//    Button(onClick = { timeScheduleState.show() }) {
//        Text(text = "gggggggggggg")
//    }
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