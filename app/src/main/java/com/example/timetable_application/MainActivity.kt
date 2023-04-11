package com.example.timetable_application

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.timetable_application.ui.MyApp
import com.example.timetable_application.entity.TimetableViewModel
import com.example.timetable_application.entity.TimetableViewModelFactory
import com.example.timetable_application.ui.theme.Timetable_ApplicationTheme

class MainActivity : ComponentActivity() {
    //申明viewModel和viewModelFactory，但不马上初始化（lateinit）
    private lateinit var viewModel: TimetableViewModel
    private lateinit var viewModelFactory: TimetableViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化
        viewModelFactory = TimetableViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory)
            .get(TimetableViewModel::class.java)
        setContent {
            Timetable_ApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(viewModel)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Timetable_ApplicationTheme {
        val viewModel: TimetableViewModel = TimetableViewModel(Application())
        MyApp(viewModel)
    }
}