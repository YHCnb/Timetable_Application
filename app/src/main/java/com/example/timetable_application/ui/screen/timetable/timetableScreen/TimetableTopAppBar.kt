package com.example.timetable_application.ui.screen.timetable.timetableScreen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.timetable_application.MyApp
import com.example.timetable_application.R
import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.TimetableViewModel
import com.example.timetable_application.utils.parseICS
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun TimetableTopAppBar(navController: NavController, vm: TimetableViewModel, currentWeek: Int,
                       courseMap:MutableMap<String, Course>, onCallSettings:()->Unit) {
    var showImportDialog by remember { mutableStateOf(false) }
    val context = MyApp.context

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val text = inputStream?.bufferedReader().use { it?.readText() }
            val newTimetable = parseICS(text)
            // 在这里显示日历事件
            runBlocking {
                vm.addTimetable(newTimetable)
                vm.setDefaultTimetable(newTimetable.name)
                navController.navigate("CourseManagement/${newTimetable.name}")
            }
            // 异步关闭输入流
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    inputStream?.close()
                }
            }
        }
    }

    ImportDialog(
        showDialog = showImportDialog,
        onDismiss = { showImportDialog=false },
        onBIT = {
            showImportDialog=false
            navController.navigate("LoginBIT")
        },
        onICS = {
            showImportDialog=false
            launcher.launch(arrayOf("text/calendar"))
        }
    )

    TopAppBar(
        title = {
            TimeColumn(currentWeek,vm)
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
                    showImportDialog = true
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
fun TimeColumn(currentWeek: Int,vm: TimetableViewModel){
    val curWeek by vm.curWeek.observeAsState()
    var isCurWeek by remember { mutableStateOf(curWeek==currentWeek+1) }
    LaunchedEffect(vm.curWeek) {
        isCurWeek = curWeek==currentWeek+1
    }
    LaunchedEffect(currentWeek) {
        isCurWeek = curWeek==currentWeek+1
    }

    val currentDate = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/M/d")
    val weekDay = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    Column {
        Text(
            text = dateFormatter.format(currentDate),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 8.dp)
        )
        Text(

            text = "${weekDay} 第${currentWeek+1}周${if(isCurWeek) "(本周)" else ""}",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp, end = 8.dp)
        )
    }
}
//导入选择dialog
@Composable
fun ImportDialog(showDialog:Boolean,onDismiss:() -> Unit,onBIT:() -> Unit,onICS:() -> Unit){
    val context = LocalContext.current

    if(showDialog){
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = "选择导入形式",
                    fontWeight = FontWeight.SemiBold
                )
            },
            dismissButton = {},
            confirmButton = {},
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Tips:\n   ICS文件目前只支持解析BIT101.cn导出的文件-->",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "BIT101",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .clickable {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("https://bit101.cn/#/")
                            context.startActivity(intent)
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 10.dp)
                        ,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            onClick = { onBIT() },
                            modifier = Modifier.padding(0.dp,0.dp,10.dp,0.dp)
                        ) {
                            Text("BIT导入")
                        }
                        Button(onClick = { onICS() }) {
                            Text("ICS导入")
                        }
                    }
                }
            }
        )
    }
}