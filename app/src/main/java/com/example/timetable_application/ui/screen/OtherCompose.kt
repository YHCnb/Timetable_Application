package com.example.timetable_application.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chargemap.compose.numberpicker.ListItemPicker
import com.example.timetable_application.entity.OneTime
import com.example.timetable_application.ui.screen.timetable.TimeScheduleTopAppBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.date_time.DateTimeDialog
import com.maxkeppeler.sheets.date_time.models.DateTimeSelection
import java.sql.Time


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OtherCompose(navController: NavController){
    val times = kotlin.collections.listOf(
        OneTime(Time(9, 0, 0), Time(9, 0, 0)),
        OneTime(Time(10, 0, 0), Time(11, 0, 0)),
        OneTime(Time(11, 0, 0), Time(12, 0, 0)),
        OneTime(Time(12, 0, 0), Time(13, 0, 0)),
        OneTime(Time(13, 0, 0), Time(14, 0, 0)),
        OneTime(Time(14, 0, 0), Time(15, 0, 0)),
        OneTime(Time(15, 0, 0), Time(16, 0, 0)),
        OneTime(Time(16, 0, 0), Time(17, 0, 0)),
        OneTime(Time(17, 0, 0), Time(18, 0, 0)),
        OneTime(Time(17, 0, 0), Time(19, 0, 0)),
        OneTime(Time(19, 0, 0), Time(20, 0, 0)),
        OneTime(Time(20, 0, 0), Time(21, 0, 0)),
        OneTime(Time(21, 0, 0), Time(22, 0, 0)),
        OneTime(Time(22, 0, 0), Time(23, 0, 0)),
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
}