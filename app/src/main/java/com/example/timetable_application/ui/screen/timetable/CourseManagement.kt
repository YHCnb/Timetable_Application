package com.example.timetable_application.ui.screen.timetable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.timetable_application.ui.screen.timetable.dialogs.MyAlertDialog


@Composable
fun CourseManagement(navController: NavController,vm: TimetableViewModel){
    val navBackStackEntry = navController.currentBackStackEntry
    val newCourse = navBackStackEntry?.savedStateHandle?.get<Course>("newCourse")
    val oldCourseName = navBackStackEntry?.savedStateHandle?.get<String>("oldCourseName")

    var showAlertDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var onChangeCourseName =""

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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "轻触编辑，长按删除",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                item {
                    if(courseMap!=null){
                        CoursesCards(navController, courseMap!!.values,
                            onDelete = {name->
                                onChangeCourseName=name
                                message = "确认要删除课程[${onChangeCourseName}]吗？此操作将不可撤销。"
                                showAlertDialog=true
                            }
                        )
                    }
                    MyAlertDialog(
                        showDialog = showAlertDialog,
                        title = "提示",
                        message = message,
                        onClose = { showAlertDialog=false }) {
                        vm.deleteCourse(onChangeCourseName)
                        showAlertDialog=false
                    }
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
                    border = BorderStroke(1.dp, Color.White),
                    colors = CardDefaults.cardColors(containerColor = Color(java.lang.Long.parseLong("FF${course.color}", 16)))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = course.name,
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(20.dp),
                        )
                    }
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