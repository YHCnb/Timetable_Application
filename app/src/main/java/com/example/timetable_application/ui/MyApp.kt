package com.example.timetable_application.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.timetable_application.BottomMenu
import com.example.timetable_application.db.DbHelper
import com.example.timetable_application.ui.screen.AnotherCompsoe
import com.example.timetable_application.ui.screen.MainCompose
import com.example.timetable_application.ui.screen.OtherCompose
import com.example.timetable_application.ui.screen.timetable.courseEditorScreen.CourseEditor
import com.example.timetable_application.ui.screen.timetable.CourseManagement
import com.example.timetable_application.ui.screen.timetable.timetableScreen.TimetableScreen
import com.example.timetable_application.entity.TimetableViewModel
import com.example.timetable_application.ui.screen.timetable.TimeScheduleEditor
import com.example.timetable_application.ui.screen.timetable.TimeTableEditor
import com.example.timetable_application.ui.screen.timetable.TimeTableManagement
import com.google.android.gms.common.util.CollectionUtils.listOf

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyApp(vm: TimetableViewModel){
    //Navigation()
    //用于保存某个可滚动的视图（如RecyclerView、ListView、ScrollView等）的滚动状态。
    val scrollState= rememberScrollState()
    //获取NavHostController实例，支持重组的
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        BottomMenu(navController = navController)
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Navigation(navController,scrollState,vm)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation(navController: NavHostController, scrollState: ScrollState,
               vm: TimetableViewModel
){
    NavHost(navController = navController, startDestination = "Main") {
        composable("Main") {
            MainCompose(navController = navController)
        }
        composable("Other") {
            OtherCompose(navController = navController)
        }
        composable("Another") {
            AnotherCompsoe(navController = navController)
        }
        composable("Timetable") {
            TimetableScreen(navController = navController, vm = vm)
        }
        composable("TimetableManagement") {
            TimeTableManagement(navController = navController, vm = vm)
        }
        composable("TimeScheduleEditor"){
            TimeScheduleEditor(navController = navController, vm = vm)
        }
        composable(
            "TimetableEditor/{timetableName}",
            arguments = listOf(navArgument("timetableName"){//传入timetableName参数
                type = NavType.StringType
            })
        ){
            val timetableName = it.arguments?.getString("timetableName")!!
            if(timetableName!=vm.timetableName.value){
                vm.changeTimetable(timetableName)//课表名不同，先改变课表，再导航
            }
            TimeTableEditor(navController = navController, vm = vm)
        }
        composable(
            "CourseManagement/{timetableName}",
            arguments = listOf(navArgument("timetableName"){//传入timetableName参数
                type = NavType.StringType
            })
        ){
            val timetableName = it.arguments?.getString("timetableName")!!
            if(timetableName!=vm.timetableName.value){
                vm.changeTimetable(timetableName)//课表名不同，先改变课表，再导航
            }
            CourseManagement(navController = navController, vm = vm)
        }
        composable(
            "CourseEditor/{courseName}",
            arguments = listOf(navArgument("courseName"){
                type = NavType.StringType
            })
        ){NavBackStackEntry->
            val courseName = NavBackStackEntry.arguments?.getString("courseName")
            if(courseName=="_"){
                CourseEditor(navController = navController, weeksOfTerm = vm.weeksOfTerm.value!! ,
                    coursesPerDay = vm.coursesPerDay.value!!,course = DbHelper.creatExampleCourse())
            }else{
                CourseEditor(navController = navController, weeksOfTerm = vm.weeksOfTerm.value!! ,
                    coursesPerDay = vm.coursesPerDay.value!!,course = vm.courseMap.value!![courseName.toString()]!!)
            }
        }
    }
}

