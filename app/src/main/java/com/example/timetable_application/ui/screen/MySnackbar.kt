package com.example.timetable_application.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MySnackbar(snackbarHostState: SnackbarHostState){
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