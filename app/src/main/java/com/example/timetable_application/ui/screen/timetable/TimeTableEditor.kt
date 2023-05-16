package com.example.timetable_application.ui.screen.timetable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.timetable_application.entity.TimetableViewModel
import com.example.timetable_application.ui.screen.MySnackbar
import com.example.timetable_application.ui.screen.timetable.dialogs.NumberDialog
import com.example.timetable_application.ui.screen.timetable.dialogs.TextDialog
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTableEditor(navController: NavController, vm: TimetableViewModel){
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    val timetableName by vm.timetableName.observeAsState()
    val startTime by vm.startTime.observeAsState()
    val curWeek by vm.curWeek.observeAsState()
    val coursesPerDay by vm.coursesPerDay.observeAsState()
    val weeksOfTerm by vm.weeksOfTerm.observeAsState()
    val courseMap by vm.courseMap.observeAsState()

    Scaffold(
        // 定义头部
        topBar = {
            TimeTableEditorTopAppBar{
                navController.popBackStack()
            }
        },
        snackbarHost = { MySnackbar(snackbarHostState = snackbarHostState) },
    ){paddingValues->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                val showTextDialog = remember { mutableStateOf(false) }
                TextDialog(
                    showDialog = showTextDialog.value,
                    title = "课表名称",
                    initialText = timetableName!!,
                    onDismiss = { showTextDialog.value=false },
                    onConfirm = {
                        showTextDialog.value=false
                        vm.editName(it)
                    }
                )
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            showTextDialog.value = true
                        }
                    }
                    .padding(16.dp)) {
                    Text(
                        text = "课表名称",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = timetableName!!)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                //时间选择
                val calendarState = UseCaseState(embedded = false)
                val showCurWeekDialog = remember { mutableStateOf(false) }
                val showCoursesPerDayDialog = remember { mutableStateOf(false) }
                val showWeeksOfTermDialog = remember { mutableStateOf(false) }
                CalendarDialog(
                    state = calendarState,
                    config = CalendarConfig(
                        monthSelection = true,
                        yearSelection = true,
                    ),
                    selection = CalendarSelection.Date { date->
                        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        vm.editStartTime(date.format(formatter))
                    }
                )
                NumberDialog(
                    title = "当前周",
                    show = showCurWeekDialog.value,
                    list = (1..weeksOfTerm!!).toList(),
                    initialIndex = curWeek!!-1,
                    onDismiss = {showCurWeekDialog.value=false},
                ) {
                    showCurWeekDialog.value=false
                    vm.editCurWeek(it)
                }
                NumberDialog(
                    title = "课程节数",
                    show = showCoursesPerDayDialog.value,
                    list = (1..25).toList(),
                    initialIndex = coursesPerDay!!-1,
                    onDismiss = {showCoursesPerDayDialog.value=false},
                ) {
                    showCoursesPerDayDialog.value=false
                    vm.editCoursesPerDay(it)
                }
                NumberDialog(
                    title = "学期周数",
                    show = showWeeksOfTermDialog.value,
                    list = (1..25).toList(),
                    initialIndex = weeksOfTerm!!-1,
                    onDismiss = {showWeeksOfTermDialog.value=false},
                ) {
                    showWeeksOfTermDialog.value=false
                    vm.editWeeksOfTerm(it)
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {//点击即可关闭
                            calendarState.show()
                        }
                    }
                    .padding(16.dp)) {
                    Text(
                        text = "学期开始时间",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = startTime!!)
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {//点击即可关闭
                            showCurWeekDialog.value = true
                        }
                    }
                    .padding(16.dp)) {
                    Text(
                        text = "当前周",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "第 ${curWeek.toString()} 周")
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {//点击即可关闭
                            showCoursesPerDayDialog.value = true
                        }
                    }
                    .padding(16.dp)) {
                    Text(
                        text = "课程节数",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${coursesPerDay.toString()} 节")
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {//点击即可关闭
                            showWeeksOfTermDialog.value = true
                        }
                    }
                    .padding(16.dp)) {
                    Text(
                        text = "学期周数",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${weeksOfTerm.toString()} 节")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            navController.navigate("CourseManagement/${timetableName}")
                        }
                    }
                    .padding(16.dp)) {
                    Text(
                        text = "管理已添加课程",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTableEditorTopAppBar(onBackPressed: () -> Unit) {
    TopAppBar(title = { Text(text = "课表数据", fontWeight = FontWeight.ExtraBold) },
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackPressed()
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