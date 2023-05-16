package com.example.timetable_application.ui.screen.timetable.timetableScreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.timetable_application.R
import com.example.timetable_application.db.DbHelper
import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.Timetable
import com.example.timetable_application.entity.TimetableViewModel
import com.example.timetable_application.ui.screen.timetable.dialogs.TextDialog
import com.example.timetable_application.utils.parseICS
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun TimetableTopAppBar(navController: NavController, vm: TimetableViewModel, courseMap:MutableMap<String, Course>, onCallSettings:()->Unit) {
    var showTextDialog by remember { mutableStateOf(false) }
    var newTimetable by remember { mutableStateOf(DbHelper.creatNewTimetable("")) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val text = inputStream?.bufferedReader().use { it?.readText() }
            newTimetable = parseICS(text)
            // 在这里显示日历事件
            runBlocking {
                showTextDialog = true
            }
            // 异步关闭输入流
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    inputStream?.close()
                }
            }
        }
    }
    TextDialog(
        showDialog = showTextDialog,
        title = "课表名称",
        initialText = "",
        onDismiss = { showTextDialog=false },
        onConfirm = { newName->
            //创建一个新的timetable
            if(vm.timetableList.value!!.find {  it.name==newName }==null){
                newTimetable.name=newName
                vm.addTimetable(newTimetable)
                vm.setDefaultTimetable(newName)
                navController.navigate("CourseManagement/${newName}")
                showTextDialog=false
            }else{

            }
        }
    )
    TopAppBar(
        title = {
            TimeColumn()
        },
        actions = {
            IconButton(
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("courseList", courseMap.values.toList())
                    //根据课程名字进入对应的CourseEditor
                    navController.navigate("CourseEditor/${"_"}")
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                )
            ) {
                Icon(imageVector  = Icons.Default.Add,contentDescription = null)
            }
            IconButton(
                onClick = {
                    launcher.launch(arrayOf("text/calendar"))
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.navigation), contentDescription = "导入课程")
            }
            IconButton(
                onClick = { onCallSettings() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                )
            ) {
                Icon(imageVector  = Icons.Default.Settings, contentDescription = null)
            }
        }
    )
}
//顶部左侧的时间title
@Composable
fun TimeColumn(){
    val currentDate = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/M/d")
    val weekDay = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    Column {
        Text(
            text = dateFormatter.format(currentDate),
//            fontSize = 20.sp,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 8.dp)
        )
        Text(
            text = weekDay,
//            fontSize = 15.sp,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp, end = 8.dp)
        )
    }
}