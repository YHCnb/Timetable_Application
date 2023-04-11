package com.example.timetable_application.ui.screen.timetable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.timetable_application.db.DbHelper
import com.example.timetable_application.entity.Timetable
import com.example.timetable_application.entity.TimetableViewModel
import com.example.timetable_application.ui.screen.MySnackbar
import com.example.timetable_application.ui.screen.timetable.dialogs.MyAlertDialog
import com.example.timetable_application.ui.screen.timetable.dialogs.TextDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTableManagement(navController: NavController, vm: TimetableViewModel){
    val timetableList by vm.timetableList.observeAsState()
    var showTextDialog by remember { mutableStateOf(false) }

    var showAlertDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var onChangeTimetableName =""
    var whichWork =0//表示showAlertDialog进行哪种操作（0为删除课表，1为设置默认课表）

    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    Scaffold(
        // 定义头部
        topBar = {
            CourseManagementTopAppBar(){
                navController.popBackStack()
            }
        },
        snackbarHost = { MySnackbar(snackbarHostState = snackbarHostState)},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showTextDialog=true
                }){
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            } } ,
//        floatingActionButtonPosition = FabPosition.End,
    ) {paddingValues->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Text(
                    text = "点击查看课表课程，长按删除",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item {
                TimetableCards(
                    navController = navController,
                    timetableList = timetableList!!,
                    onDelete = {name->
                        if (name==vm.timetableName.value){//当前课表无法删除
                            scope.launch {
                                snackbarHostState.showSnackbar("默认课表无法删除")
                            }
                        }else{
                            whichWork = 0
                            onChangeTimetableName=name
                            message = "确认要删除课表[${onChangeTimetableName}]吗？此操作将不可撤销。"
                            showAlertDialog=true
                        }
                    }){name->
                    if (vm.timetableName.value!=name){
                        whichWork = 1
                        onChangeTimetableName=name
                        message = "确认要设置课表[${onChangeTimetableName}]为默认课表吗？"
                        showAlertDialog=true
                    }
                }
            }
            item {
                //用于新建课表的提示框
                TextDialog(
                    showDialog = showTextDialog,
                    title = "课表名称",
                    initialText = "",
                    onClose = { showTextDialog=false },
                    onTextChanged = { newName->
                        //创建一个新的timetable
                        if(timetableList!!.find {  it.name==newName }==null){
                            vm.addTimetable(timetable = DbHelper.creatNewTimetable(newName))
                        }else{
                            scope.launch {
                                snackbarHostState.showSnackbar("课表重名")
                            }
                        }
                    }
                )
                //用于提示是否删除,是否设置此为默认课表
                MyAlertDialog(
                    showDialog = showAlertDialog,
                    title = "提示",
                    message = message,
                    onClose = { showAlertDialog=false }) {
                    if (whichWork==0){
                        vm.deleteTimetable(onChangeTimetableName)
                        showAlertDialog=false
                    }else if(whichWork==1){
                        vm.setDefaultTimetable(onChangeTimetableName)
                        showAlertDialog=false
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimetableCards(navController: NavController, timetableList: List<Timetable>, onDelete: (String) -> Unit,onSetDefault:(String)->Unit){
    Column() {
        timetableList.forEach{ timetable->
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .height(100.dp)
                    .fillMaxWidth()
                    .combinedClickable(
                        enabled = true,
                        onClick = {
                            //根据课表名字进入对应的CourseManagement
                            navController.navigate("CourseManagement/${timetable.name}")
                        },
                        onLongClick = { onDelete(timetable.name) }
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(

                ) {
                    Text(
                        text = timetable.name,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(16.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        IconButton(onClick = { onSetDefault(timetable.name) }) {
                            Icon(imageVector = Icons.Filled.Favorite, contentDescription="defaultTimetable")
                        }
                        IconButton(onClick = {  }) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription="editTimetable")
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableManagementTopAppBar(onBackPressed: () -> Unit = {}) {
    TopAppBar(title = { Text(text = "课表管理", fontWeight = FontWeight.ExtraBold) },
        navigationIcon = {
            IconButton(
                onClick = { onBackPressed() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                )
            ) {
                Icon(imageVector  = Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}