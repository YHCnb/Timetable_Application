package com.example.timetable_application.ui.screen.timetable.timetableScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingFloatingBox(visible:Boolean,onDismiss: () -> Unit,onCallTimetableManager: () -> Unit){
    BoxWithConstraints (){
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { 300 }),
            exit = slideOutVertically(targetOffsetY = { 300 })
        ) {
            val maxWidth = maxWidth.div(1.1.toFloat())
            Column() {
                Box(
                    modifier = Modifier
                        .width(maxWidth)
                        .height(100.dp)
                        .background(Color.Blue.copy(alpha = 0.2f))
                        .clickable {
                            onDismiss()
                        }
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Button(onClick = { onCallTimetableManager() }) {
                        
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .width(maxWidth)
                        .height(100.dp)
                        .background(Color.Blue.copy(alpha = 0.2f))
                        .clickable {
                            onDismiss()
                        }
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    
                }
            }
        }
    }
}