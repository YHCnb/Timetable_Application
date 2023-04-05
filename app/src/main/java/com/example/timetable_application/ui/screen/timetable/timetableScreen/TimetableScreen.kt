package com.example.timetable_application.ui.screen.timetable.timetableScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.TimetableViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TimetableScreen(navController: NavController,vm: TimetableViewModel) {
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
    val curWeek by vm.curWeek.observeAsState()
    val coursesPerDay by vm.coursesPerDay.observeAsState()
    val weeksOfTerm by vm.weeksOfTerm.observeAsState()
    val courseMap by vm.courseMap.observeAsState()

    val currentWeek = rememberPagerState(initialPage = curWeek!!)
    val weekDays = listOf("一", "二", "三", "四", "五", "六", "日")
    val dates = generateWeekDates(startDate = LocalDate.parse(startTime), weeksOfTerm = weeksOfTerm!!)

    Scaffold(
        topBar = { TimetableTopAppBar(navController=navController,courseMap=courseMap!!) }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
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
                    currentWeek = page+1
                )
            }
        }
    }
//    OutlinedButton(
//        onClick = { navController.navigate(
//            "CourseManagement/${timetableName}",
//        ) },
//    ) {
//        Text(text = "go to 课程管理")
//    }
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
