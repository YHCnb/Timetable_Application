package com.example.timetable_application

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.timetable_application.db.DbHelper
import com.example.timetable_application.entity.TimetableViewModel
import com.example.timetable_application.entity.TimetableViewModelFactory
import com.example.timetable_application.ui.screen.About
import com.example.timetable_application.ui.screen.LoginBIT
import com.example.timetable_application.ui.screen.timetable.CourseManagement
import com.example.timetable_application.ui.screen.timetable.TimeScheduleEditor
import com.example.timetable_application.ui.screen.timetable.TimeTableEditor
import com.example.timetable_application.ui.screen.timetable.TimeTableManagement
import com.example.timetable_application.ui.screen.timetable.courseEditorScreen.CourseEditor
import com.example.timetable_application.ui.screen.timetable.timetableScreen.TimetableScreen
import com.example.timetable_application.ui.theme.Timetable_ApplicationTheme
import com.google.android.gms.common.util.CollectionUtils
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    //申明viewModel和viewModelFactory，但不马上初始化（lateinit）
    private lateinit var viewModel: TimetableViewModel
    private lateinit var viewModelFactory: TimetableViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
//        installSplashScreen()
        super.onCreate(savedInstanceState)
        val viewModelStoreOwner = this
        //初始化
        viewModelFactory = TimetableViewModelFactory(application)
        viewModel = ViewModelProvider(viewModelStoreOwner,viewModelFactory)
            .get(TimetableViewModel::class.java)
        setContent {
            Timetable_ApplicationTheme {
                var boolValue by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(1000)
                    boolValue = true
                }
                val navController = rememberNavController()
                if (boolValue){
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Scaffold(
//                            bottomBar = { BottomMenu(navController = navController) }
                        ) { padding ->
                            Column(modifier = Modifier.padding(padding)) {
                                Navigation(navController,viewModel)
                            }
                        }
                    }
                }else{
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFFd7ebd0)
                    ) {
                        Column(
                            verticalArrangement= Arrangement.Center,
                            horizontalAlignment= Alignment.CenterHorizontally,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.logo),
                                    contentDescription= "Image",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation(navController: NavHostController,
               vm: TimetableViewModel
){
    NavHost(navController = navController, startDestination = "Timetable") {
        composable("Timetable") {
            TimetableScreen(navController = navController, vm = vm)
        }
        composable("LoginBIT") {
            LoginBIT(navController = navController,vm = vm)
        }
        composable("About") {
            About(navController = navController)
        }
        composable("TimetableManagement") {
            TimeTableManagement(navController = navController, vm = vm)
        }
        composable("TimeScheduleEditor"){
            TimeScheduleEditor(navController = navController, vm = vm)
        }
        composable(
            "TimetableEditor/{timetableName}",
            arguments = CollectionUtils.listOf(navArgument("timetableName"){//传入timetableName参数
                type = NavType.StringType
            })
        ){
            val timetableName = it.arguments?.getString("timetableName")!!
//            if(timetableName!=vm.timetableName.value){
            vm.changeTimetable(timetableName)//课表名不同，先改变课表，再导航
//            }
            TimeTableEditor(navController = navController, vm = vm)
        }
        composable(
            "CourseManagement/{timetableName}",
            arguments = CollectionUtils.listOf(navArgument("timetableName"){//传入timetableName参数
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
            arguments = CollectionUtils.listOf(navArgument("courseName"){
                type = NavType.StringType
            })
        ){NavBackStackEntry->
            val courseName = NavBackStackEntry.arguments?.getString("cuorseName")
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