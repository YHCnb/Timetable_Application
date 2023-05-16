package com.example.timetable_application.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.substring
import androidx.navigation.NavController
import com.example.timetable_application.db.DbHelper
import com.example.timetable_application.entity.Course
import com.example.timetable_application.entity.CourseTime
import com.example.timetable_application.entity.TemplateColors
import com.example.timetable_application.entity.Timetable
import com.example.timetable_application.utils.parseICS
import kotlinx.coroutines.*
import org.apache.commons.lang3.ObjectUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun MainCompose(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "it is MainCompose", fontWeight = FontWeight.SemiBold)
        Button(onClick = {
            navController.navigate("Another")
        }) {
            Text(text = "go to Another")
        }
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                val inputStream = context.contentResolver.openInputStream(uri)
                val text = inputStream?.bufferedReader().use { it?.readText() }
                val events = parseICS(text)
                // 在这里显示日历事件
                runBlocking {
                    println(events)
                }
                // 异步关闭输入流
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        inputStream?.close()
                    }
                }
            }
        }
        Button(onClick = { launcher.launch(arrayOf("text/calendar")) }) {
            Text("选择 ICS 文件")
        }
    }
}