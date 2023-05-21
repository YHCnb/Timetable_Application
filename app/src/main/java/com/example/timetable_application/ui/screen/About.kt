package com.example.timetable_application.ui.screen

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.timetable_application.*
import com.example.timetable_application.R

@Composable
fun About(navController: NavController){
    val context = LocalContext.current

    Scaffold(
        // 定义头部
        topBar = {
            AnotherTopAppBar(){
                //  返回
                navController.popBackStack()
            }
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    contentAlignment= Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription= "Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        modifier = Modifier.padding(0.dp,5.dp),
                        text = "1.感谢金旭亮老师在安卓技术上给予的指导",
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                    )
                    Text(
                        modifier = Modifier.padding(0.dp,5.dp),
                        text = "2.学校网站访问流程由范同学提供，在此特别鸣谢(๑•̀ㅂ•́)و✧",
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                    )
                    Text(
                        modifier = Modifier.padding(0.dp,5.dp),
                        text = "3.应用logo的灵感来源与创作全程来自New Bing(赞)",
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                    )
                    Text(
                        modifier = Modifier.padding(0.dp,5.dp),
                        text = "4.应用性能仍在优化，功能尚未完善",
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                    )
                    Text(
                        modifier = Modifier.padding(0.dp,5.dp),
                        text = "5.如有任何问题或者建议，欢迎通过邮箱或者Github进行联系",
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                    )
                    Spacer(modifier = Modifier.padding(20.dp))
                    Row(

                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp,0.dp),
                            text = "Github --->",
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                        )
                        Text(
                            text = "Classy Timetable",
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse("https://github.com/YHCnb/Timetable_Application")
                                context.startActivity(intent)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row() {
                        Text(
                            modifier = Modifier.padding(10.dp,0.dp),
                            text = "邮箱 --->",
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                        )
                        Text(
                            text = "1262736427@qq.com",
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row() {
                        Text(
                            modifier = Modifier.padding(10.dp,0.dp),
                            text = "范大神的网站 --->",
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                        )
                        Text(
                            text = "BIT101",
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse("https://bit101.cn/#/")
                                context.startActivity(intent)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    textAlign = TextAlign.Center,
                    text = "Classy Timetable: 1.3",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleMedium
                )
            }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnotherTopAppBar(onBackPressed: () -> Unit = {}) {
    TopAppBar(title = { Text(text = "About", fontWeight = FontWeight.ExtraBold) },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(imageVector = Icons.Default.ArrowBack, "")
            }
        }
    )
}