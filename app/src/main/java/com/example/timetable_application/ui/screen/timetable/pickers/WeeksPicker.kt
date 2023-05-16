package com.example.timetable_application.ui.screen.timetable.pickers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.timetable_application.R

@Composable
fun WeeksPicker(
    weeksOfTerm:Int,
    initialWeeks:List<Int>,
    onWeeksChanged:(newWeeks: List<Int>) -> Unit,
){
    val selectedWeeks = remember { mutableStateListOf<Int>() }
    LaunchedEffect(Unit) {
        initialWeeks.forEach {
            selectedWeeks.add(it)
        }
    }
    val showDialog = remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .clickable { showDialog.value = true }
            .height(50.dp)
            .padding(10.dp)
    ) {
        Row(
            Modifier.fillMaxHeight()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plan),
                contentDescription = null,
//                modifier = Modifier.fillMaxSize(),
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = selectedWeeksText(selectedWeeks),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
    if(showDialog.value){
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Choose weeks") },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                ) {
                    for (i in 1..weeksOfTerm){
                        item {
//                            val isSelected = remember { mutableStateOf(selectedWeeks.contains(i)) }
                            var isSelected = selectedWeeks.contains(i)
                            TextButton(
                                onClick = {
                                    if (isSelected) {
                                        selectedWeeks.remove(i)
                                    } else {
                                        selectedWeeks.add(i)
                                    }
                                    isSelected=!isSelected
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(//根据是否选中呈现不同颜色
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
                                    contentColor = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(6.dp)
                            ) {
                                Text(text = i.toString())
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedWeeks.sort()
                        onWeeksChanged(selectedWeeks.toList())
                        showDialog.value = false
                    }
                ) {
                    Text(
                        text = "确定",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(
                        text = "取消",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            },
        )
    }
}
fun selectedWeeksText(selectedWeeks:List<Int>):String{
    selectedWeeks.sorted()
    var pre = -1
    var start = -1//表示没有开头，此时为第一个
    var s = ""
    selectedWeeks.forEach{
        if (it!=pre+1){
            if(start!=-1){
                if(s!="") {s+=","}
                if(pre!=start){
                    s+="第${start}-${pre}周"
                }else{
                    s+="第${pre}周"
                }
            }
            start = it
        }
        pre =it
    }
    if(start!=-1){
        if(s!="") {s+=","}
        if(pre!=start){
            s+="第${start}-${pre}周"
        }else{
            s+="第${pre}周"
        }
    }
    return s
}