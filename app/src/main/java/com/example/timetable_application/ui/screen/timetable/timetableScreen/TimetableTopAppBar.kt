package com.example.timetable_application.ui.screen.timetable.timetableScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.timetable_application.entity.Course
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableTopAppBar(navController: NavController,courseMap:MutableMap<String, Course>,onCallSettings:()->Unit) {
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
                // Use IconButtonColors function to create a material3 style
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                )
            ) {
                Icon(imageVector  = Icons.Default.Add,contentDescription = null)
            }
            IconButton(
                onClick = { onCallSettings() },
                // Use IconButtonColors function to create a material3 style
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