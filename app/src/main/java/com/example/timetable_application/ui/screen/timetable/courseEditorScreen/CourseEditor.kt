package com.example.timetable_application.ui.screen.timetable.courseEditorScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.CourseTime
import com.example.timetable_application.ui.screen.MySnackbar
import com.example.timetable_application.ui.screen.timetable.dialogs.SaveOrLeaveDialog
import com.example.timetable_application.ui.screen.timetable.pickers.ColorPicker
import kotlinx.coroutines.launch

//需要同时完成两个目标，修改课程与增加课程
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseEditor(navController: NavController,weeksOfTerm:Int,coursesPerDay:Int, course: Course){
    var nameState by remember() {mutableStateOf(course.name)}
    var colorState by remember() { mutableStateOf(course.color) }
    val timesState = remember() { mutableStateListOf<CourseTime>(*course.time.toTypedArray()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .clickable(//使得动画效果不显示
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ){//收起键盘
                focusManager.clearFocus()
            },
        // 定义头部
        topBar = {
            EditorTopAppBar(
                onBackPressed = {navController.popBackStack()}
            ){//onConfirm()
                val courseList = navController.previousBackStackEntry?.savedStateHandle?.get<List<Course>>("courseList")
                if(courseList!=null){
                    //创建newCourse
                    val newCourse = Course(name = nameState,color = colorState, time = timesState.toMutableStateList())
                    if(newCourse.name==""){
                        scope.launch { snackbarHostState.showSnackbar("课程名不能为空！") }
                    }else if(newCourse.name!=course.name && courseList.any { it.name == newCourse.name }){
                        scope.launch { snackbarHostState.showSnackbar("课程名称冲突！") }
                    }else{
                        //判断是否有课程冲突
                        var isConflict = false
                        courseList.forEach{
                            if(it.name!=course.name){
                                it.time.forEach{ courseTime ->
                                    newCourse.time.forEach{newCourseTime->
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
                        if(isConflict){
                            scope.launch { snackbarHostState.showSnackbar("课程冲突") } }
                        else{
                            //如果都没错误，则返回newCourse和原来course的名字，方便更新
                            navController.previousBackStackEntry?.savedStateHandle?.set("newCourse", newCourse)
                            navController.previousBackStackEntry?.savedStateHandle?.set("oldCourseName", course.name)
                            navController.popBackStack()
                        }
                    }
                }else{
                    scope.launch{snackbarHostState.showSnackbar("未接收到courseMap，保存失败")}
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
                        modifier = Modifier.fillMaxWidth(),
                        colors= TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                }
                item { ColorPicker(initialColor = colorState, onColorChanged = {colorState=it}) }
                itemsIndexed(timesState){ index, _ ->
                    Divider(
                        color = MaterialTheme.colorScheme.surface,
                        thickness = 2.dp,
                    )
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
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    timesState.add(CourseTime())
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        },
        snackbarHost = {
            MySnackbar(snackbarHostState = snackbarHostState)
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
