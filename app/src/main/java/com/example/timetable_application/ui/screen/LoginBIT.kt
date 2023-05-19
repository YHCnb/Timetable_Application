package com.example.timetable_application.ui.screen

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.timetable_application.R
import com.example.timetable_application.entity.TimetableViewModel
import com.example.timetable_application.utils.*
import kotlinx.coroutines.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBIT(navController: NavController,vm: TimetableViewModel){
    val context = LocalContext.current

    var username by remember() { mutableStateOf("") }
    var password by remember() { mutableStateOf("") }
    var message by remember() { mutableStateOf("") }
    var islogin by remember() { mutableStateOf(false) }
    LaunchedEffect(key1 = "checklogin"){
        checkWebvpn()
        islogin = checkLogin()
        if (islogin) message = "已经登录，可直接获取课表:)"
    }
    Scaffold (
        topBar = {
            TopAppBar(){
                navController.popBackStack()
            }
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription= "Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
            TextField(
                value = username,
                onValueChange = { username=it },
                label = { Text("学号") },
                modifier = Modifier.fillMaxWidth(),
                colors= TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
            TextField(
                value = password,
                onValueChange = { password=it },
                label = { Text("密码") },
                modifier = Modifier.fillMaxWidth(),
                colors= TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
            Text(text = message, fontWeight = FontWeight.SemiBold)
            Row(){
                Button(onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        if(!islogin){
                            if(username==""){
                                message = "学号不能为空!"
                            }else if(password==""){
                                message = "密码不能为空!"
                            }else{
                                message = ""
                                islogin = login(username,password)
                                if (islogin){
                                    message = "sucess login!\n请耐心等待，这可能会花费一段时间:)"
                                    val t = getCourseSchedule()
                                    if (t==null){
                                        Log.e("getCourseSchedule","CourseScheduleResponse is null")
                                        message = "出现意料外错误，请稍后重试"
                                    }else{
                                        val newTimetable = parselWebResponse(t)
                                        vm.addTimetable(newTimetable)
                                        vm.setDefaultTimetable(newTimetable.name)
                                        navController.navigate("CourseManagement/${newTimetable.name}")
                                    }
                                }else{
                                    message = "fail login!\n请输入正确的学号/密码"
                                }
                            }
                        }else{
                            login(username,password)
                            message = "sucess login!\n请耐心等待，这可能会花费一段时间:)"
                            val t = getCourseSchedule()
                            if (t==null){
                                Log.e("getCourseSchedule","CourseScheduleResponse is null")
                                message = "出现意料外错误，请稍后重试"
                            }else{
                                runBlocking {
                                    val newTimetable = parselWebResponse(t)
                                    vm.addTimetable(newTimetable)
                                    vm.setDefaultTimetable(newTimetable.name)
                                    vm.changeTimetable()
                                    navController.navigate("CourseManagement/${newTimetable.name}")
                                }
                            }
                        }
                    }
                }) {
                    Text("获取课表")
                }
                if(islogin){
                    Button(
                        onClick = {
                        logout()
                        message = "帐号成功登出"
                        islogin=false
                        },
                        modifier = Modifier.padding(start = 10.dp)
                    ){
                        Text("帐号登出")
                    }
                }
            }
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Tips:\n   你的账号信息十分安全，密码将通过特殊的加密传入学校服务器。首次登录后短期内无需再次登录，这与Cookie有关，并不意味着本软件" +
                        "对你的信息有任何的物理保存。\n   对于学校网站接口的访问流程由范大神无私提供,在此表达由衷的感谢Orz，大神开发的网站请看下方链接-->",
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "BIT101",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://bit101.cn/#/")
                    context.startActivity(intent)
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(onBackPressed: () -> Unit = {}) {
    TopAppBar(title = { Text(text = "获取学校课表", fontWeight = FontWeight.ExtraBold) },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(imageVector = Icons.Default.ArrowBack, "")
            }
        }
    )
}