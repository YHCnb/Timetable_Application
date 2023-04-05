package com.example.timetable_application.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OtherCompose(navController: NavController){
    val state = rememberPagerState(initialPage = 5)
    Column() {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            count = 10
        ) { page ->
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(Color.Blue)
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(text = page.toString(), fontSize = 32.sp)
            }
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                state.scrollToPage(page=state.currentPage-1)
            }
        }) {
            Text(text = "pre page")
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                state.scrollToPage(page=state.currentPage+1)
            }
        }) {
            Text(text = "next page")
        }
    }
}