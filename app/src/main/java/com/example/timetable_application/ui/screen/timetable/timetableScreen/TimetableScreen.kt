package com.example.timetable_application.ui.screen.timetable.timetableScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.timetable_application.db.DbHelper
import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.TimetableViewModel
import com.example.timetable_application.ui.screen.timetable.pickers.DrawerNumberPicker
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TimetableScreen(navController: NavController,vm: TimetableViewModel) {
    vm.changeTimetable()//使其设置为默认课表

    val navBackStackEntry = navController.currentBackStackEntry
    val newCourse = navBackStackEntry?.savedStateHandle?.get<Course>("newCourse")
    val oldCourseName = navBackStackEntry?.savedStateHandle?.get<String>("oldCourseName")

    if (newCourse!=null && oldCourseName!=null){//更新course
        if(oldCourseName==""){
            vm.addCourse(newCourse)
        }else{
            vm.updateCourse(oldCourseName,newCourse)
        }
    }

    //将livedata转换为compose可以观察的状态
    val timetableName by vm.timetableName.observeAsState()
    val startTime by vm.startTime.observeAsState()
    val coursesPerDay by vm.coursesPerDay.observeAsState()
    val weeksOfTerm by vm.weeksOfTerm.observeAsState()
    val courseMap by vm.courseMap.observeAsState()
    val curWeek by vm.curWeek.observeAsState()
    val currentWeek = rememberPagerState(initialPage = curWeek!!-1)
    LaunchedEffect(vm.curWeek) {
        currentWeek.scrollToPage(vm.curWeek.value!!-1)
    }
    val weekDays = listOf("一", "二", "三", "四", "五", "六", "日")
    val dates = generateWeekDates(startDate = LocalDate.parse(startTime), weeksOfTerm = 25)
    //用于Drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet (
                modifier = Modifier.width(250.dp)
                    ){
                Text(
                    text = "全局管理",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(10.dp),
                )
                NavigationDrawerItem(
                    label = {
                        Text(text = "课表管理")
                    },
                    selected = false,
                    onClick = {
                        scope.launch {//点击即可关闭
                            drawerState.close()
                            navController.navigate("TimetableManagement")
                        }
                    }
                )
                NavigationDrawerItem(
                    label = {
                        Text(text = "课程管理")
                    },
                    selected = false,
                    onClick = {
                        scope.launch {//点击即可关闭
                            drawerState.close()
                            navController.navigate("CourseManagement/${timetableName}")
                        }
                    },
                )
                NavigationDrawerItem(
                    label = {
                        Text(text = "时间表")
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            navController.navigate("TimeScheduleEditor")
                        }
                    }
                )
                Divider(
                    color = MaterialTheme.colorScheme.onSurface,
                    thickness = 2.dp,
                )
                Text(
                    text = "基本设置",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(10.dp),
                )
                //时间选择
                val calendarState = UseCaseState(
                    embedded = false,
                    onDismissRequest = {
                        scope.launch {//点击即可关闭
                            drawerState.open()
                        }
                    },
                    onCloseRequest = {
                        scope.launch {//点击即可关闭
                            drawerState.open()
                        }
                    }
                )
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
                NavigationDrawerItem(
                    label = {
                        Text(text = "学期开始时间")
                    },
                    selected = false,
                    onClick = {
                        scope.launch {//点击即可关闭
                            drawerState.close()
                            calendarState.show()
                        }
                    }
                )
                DrawerNumberPicker(
                    title = "当前周", list = (1..weeksOfTerm!!).toList(), initialIndex = curWeek!!-1,
                    onClose = {
                        scope.launch {//点击即可关闭
                            drawerState.close()
                        }
                    },
                    onConfirm = {
                        scope.launch{
                            currentWeek.scrollToPage(it-1)
                        }
                        vm.editCurWeek(it)
                    }
                ) {
                    scope.launch {//点击即可关闭
                        drawerState.open()
                    }
                }
                DrawerNumberPicker(
                    title = "课程节数", list = (1..25).toList(), initialIndex = coursesPerDay!!-1,
                    onClose = {
                        scope.launch {//点击即可关闭
                            drawerState.close()
                        }
                    },
                    onConfirm = { vm.editCoursesPerDay(it) }
                ) {
                    scope.launch {//点击即可关闭
                        drawerState.open()
                    }
                }
                DrawerNumberPicker(
                    title = "学期周数", list = (1..25).toList(), initialIndex = weeksOfTerm!!-1,
                    onClose = {
                        scope.launch {//点击即可关闭
                            drawerState.close()
                        }
                    },
                    onConfirm = { vm.editWeeksOfTerm(it) }
                ) {
                    scope.launch {//点击即可关闭
                        drawerState.open()
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Divider(
                    color = MaterialTheme.colorScheme.onSurface,
                    thickness = 2.dp,
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "关于",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    selected = false,
                    onClick = {
                        scope.launch {//点击即可关闭
                            drawerState.close()
                        }
                        navController.navigate("About")
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TimetableTopAppBar(navController=navController, vm = vm, courseMap=courseMap!!,currentWeek = currentWeek.currentPage,
                    onCallSettings = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        ) {
            val timeSchedule = DbHelper.creatTimeSchedule()
            Column(
                modifier = Modifier.padding(it)
            ) {
                // 每周课表
                HorizontalPager(
                    modifier = Modifier.fillMaxWidth(),
                    state = currentWeek,
                    count = weeksOfTerm!!
                ) { page ->
                    OneWeekTimetable(
                        navController = navController,
                        courseMap = courseMap!!,
                        currentWeekDates = dates[page],
                        weekDays = weekDays,
                        currentWeek = page+1,
                        coursesPerDay = coursesPerDay!!,
                        timeSchedule = timeSchedule
                    )
                }
            }
        }
    }
}
// 生成一个月份及其对应的日期列表
fun generateWeekDates(startDate:LocalDate ,weeksOfTerm:Int): List< List<LocalDate> > {
    val dates = mutableListOf< List<LocalDate> >()
    for (i in 0 until weeksOfTerm) {
        val weekStart = startDate.plusWeeks(i.toLong())
            .with(DayOfWeek.MONDAY)
        val newWeek = mutableListOf<LocalDate>()
        for(j in 0 .. 6){
            newWeek.add(weekStart.plusDays(j.toLong()))
        }
        dates.add(newWeek)
    }
    return dates
}
