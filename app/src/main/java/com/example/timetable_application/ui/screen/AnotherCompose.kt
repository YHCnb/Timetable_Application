package com.example.timetable_application.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnotherCompsoe(navController: NavController){
    Scaffold(
        // 定义头部
        topBar = {
            AnotherTopAppBar(){
                //  返回
                navController.popBackStack()
            }
        }) {padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "it is AnotherCompsoe", fontWeight = FontWeight.SemiBold)
                Button(onClick = {
                    navController.popBackStack()
                }) {
                    Text(text = "go back")
                }
            }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnotherTopAppBar(onBackPressed: () -> Unit = {}) {
    TopAppBar(title = { Text(text = "Another Page", fontWeight = FontWeight.ExtraBold) },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(imageVector = Icons.Default.ArrowBack, "")
            }
        })
}