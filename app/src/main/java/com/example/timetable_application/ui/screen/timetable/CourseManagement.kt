package com.example.timetable_application.ui.screen.timetable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.TimetableViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseManagement(navController: NavController,vm: TimetableViewModel){
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
    val courseMap by vm.courseMap.observeAsState()

    Box(
        modifier = Modifier
            .height(600.dp)
    ) {
        Scaffold(
            // 定义头部
            topBar = {
                CourseManagementTopAppBar(){
                    navController.popBackStack()
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("courseList", courseMap!!.values.toList())
                        //根据课程名字进入对应的CourseEditor
                        navController.navigate("CourseEditor/${"_"}")
                    }){
                    Icon(Icons.Filled.Add, contentDescription = "Localized description")
                } } ,
            floatingActionButtonPosition = FabPosition.End,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "轻触编辑，长按删除",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
                if(courseMap!=null){
                    CoursesCards(navController, courseMap!!.values,
                        onDelete = {deleteName->
                            vm.deleteCourse(deleteName)
                        }
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoursesCards(navController: NavController, courseMap: MutableCollection<Course>, onDelete: (String) -> Unit){
    val courseList = courseMap.toList()
    val chunkedList = courseList.chunked(2)

    chunkedList.forEach{ chunk->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            chunk.forEach{course->
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .width(160.dp)
                        .height(85.dp)
                        .combinedClickable(
                            enabled = true,
                            onClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "courseList",
                                    courseList
                                )
                                //根据课程名字进入对应的CourseEditor
                                navController.navigate("CourseEditor/${course.name}")
                            },
                            onLongClick = { onDelete(course.name) }
                        ),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = CardDefaults.cardColors(containerColor = Color(java.lang.Long.parseLong("FF${course.color}", 16)))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = course.name,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(20.dp),
                        )
                    }
//                    Text(
//                        text = course.name,
//                        fontWeight = FontWeight.Normal,
//                        modifier = Modifier.padding(20.dp),
//                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseManagementTopAppBar(onBackPressed: () -> Unit = {}) {
    TopAppBar(title = { Text(text = "课程管理", fontWeight = FontWeight.ExtraBold) },
        navigationIcon = {
            IconButton(
                onClick = { onBackPressed() },
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