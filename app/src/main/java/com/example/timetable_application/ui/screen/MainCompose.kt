package com.example.timetable_application.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.timetable_application.ui.screen.timetable.timetableScreen.SettingFloatingBox

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
    }
}