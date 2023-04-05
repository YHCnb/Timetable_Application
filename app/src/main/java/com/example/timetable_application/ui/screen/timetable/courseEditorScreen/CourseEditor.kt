package com.example.timetable_application.ui.screen.timetable.courseEditorScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.CourseTime
import com.example.timetable_application.ui.screen.timetable.courseEditorScreen.pickers.ColorPicker

//需要同时完成两个目标，修改课程与增加课程
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseEditor(navController: NavController,weeksOfTerm:Int,coursesPerDay:Int, course: Course){
    var nameState by remember() {mutableStateOf(course.name)}
    var colorState by remember() { mutableStateOf(course.color) }
    val timesState = remember() { mutableStateListOf<CourseTime>(*course.time.toTypedArray()) }
    var message by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(scope){
        if (message!=""){
            snackbarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        // 定义头部
        topBar = {
            EditorTopAppBar(
                onBackPressed = {navController.popBackStack()}
            ){//onConfirm()
                val courseList = navController.previousBackStackEntry?.savedStateHandle?.get<List<Course>>("courseList")
                if(courseList!=null){
                    //创建newCourse
                    val newCourse = Course(name = nameState,color = colorState, time = timesState.toMutableList())
                    if(newCourse.name==""){
                        message = "课程名不能为空！"
                    }else if(newCourse.name!=course.name && courseList.any { it.name == newCourse.name }){
                        message="课程名称冲突！"
                    }else{
                        //判断是否有课程冲突
                        var isConflict = false
                        courseList.forEach{
                            if(it.name!=course.name){
                                it.time.forEach{ courseTime ->
                                    newCourse.time.forEach{newCourseTime->
                                        println(newCourseTime.dayOfWeek)
                                        if(newCourseTime.dayOfWeek==courseTime.dayOfWeek){
                                            newCourseTime.timeOfCourse.forEach{nt->
                                                if (courseTime.timeOfCourse.contains(nt)){
                                                    isConflict=true
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(isConflict){ message="课程冲突"}
                        else{
                            //如果都没错误，则返回newCourse和原来course的名字，方便更新
                            navController.previousBackStackEntry?.savedStateHandle?.set("newCourse", newCourse)
                            navController.previousBackStackEntry?.savedStateHandle?.set("oldCourseName", course.name)
                            navController.popBackStack()
                        }
                    }
                }else{
                    message="未接收到courseMap，保存失败"
                    navController.popBackStack()
                }
            }
        },
        content = { PaddingValues->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues),
            ) {
                item {
                    TextField(
                        value = nameState,
                        onValueChange = { nameState=it },
                        label = { Text("课程名字") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item { ColorPicker(initialColor = colorState, onColorChanged = {colorState=it}) }
                itemsIndexed(timesState){ index, _ ->
                    CourseTimeEditor(
                        time = timesState[index],
                        weeksOfTerm = weeksOfTerm,
                        coursesPerDay = coursesPerDay,
                        onTimeChanged = { newTime ->
                            timesState[index]=newTime
                        },
                        onDeleteTime = {
                            timesState.removeAt(index)
                        }
                    )
                }
                item {
                    Button(
                        onClick = {
                            timesState.add(CourseTime())
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("添加时间段")
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        action = {
                            TextButton(onClick = { snackbarHostState.currentSnackbarData?.dismiss() }) {
                                Text(text = "Dismiss")
                            }
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(data.visuals.message)
                    }
                }
            )
        },
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopAppBar(onBackPressed: () -> Unit,onConfirm: () -> Unit) {
    //开始处于隐藏状态
    val openDialog = remember { mutableStateOf(false) }
    SaveOrLeaveDialog(openDialog = openDialog, onConfirm = {onConfirm()},onDismiss = {onBackPressed()})

    TopAppBar(title = { Text(text = "添加课程", fontWeight = FontWeight.ExtraBold) },
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
