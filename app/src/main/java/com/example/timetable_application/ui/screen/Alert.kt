package com.example.timetable_application.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun Alert(snackbarHostState: SnackbarHostState,message: String){
    val scope = rememberCoroutineScope()
    LaunchedEffect(scope) {
        if(message!=""){
            snackbarHostState.showSnackbar(message)
        }
    }
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
}