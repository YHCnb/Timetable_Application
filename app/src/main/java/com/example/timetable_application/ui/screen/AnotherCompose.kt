package com.example.timetable_application.ui.screen

import android.content.res.AssetManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.timetable_application.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import org.mozilla.javascript.ScriptableObject.callMethod
import retrofit2.Call
import retrofit2.Response
import java.io.BufferedReader
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDate
import kotlin.concurrent.thread


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnotherCompsoe(navController: NavController){
    var text by remember { mutableStateOf("")}
    val service: MyWebService = MyWebService.service
    fun initLogin(callback: (result: Map<String, String?>) -> Unit) {
        //Android规定，要在独立的线程中访问网络，可以试一试，如果不满足这点
        //将得到一个NetworkOnMainThreadException
        thread {
            //Call对象封装了向Web Server端发出HTTP请求，并取回结果的功能
            val result: Call<ResponseBody> = service.doPost(login_url)
            try {
                val response = result.execute()
                val soup = response.body()?.string()?.let { Jsoup.parse(it) }
                val pwdFrom = soup!!.getElementById("pwdFromId")!!
                val salt = pwdFrom.getElementById("pwdEncryptSalt")?.attr("value")
                val execution = pwdFrom.getElementById("execution")?.attr("value")
                val cookie = response.headers()["Set-Cookie"]
                //提取出完整的从server端发回的Json字符串
                val r = mapOf(
                    "salt" to salt,
                    "execution" to execution,
                    "cookie" to cookie
                )
//                text = "sucess:${r}"
                callback(r)
            } catch (e: Exception) {
                text = "fail:${e.message ?: e.toString()}"
                println(e.message ?: e.toString())
            }
        }
    }
    // 查询是否需要验证码
    fun needCaptcha(sid: String,callback: (result: Boolean) -> Unit) {
        thread {
            val result: Call<ResponseBody> = service.doPost("$need_captcha_url?username=$sid")
            try {
                val response = result.execute()
                val json = response.body()?.string() ?: ""
                val r = json.contains("\"isNeed\":true")
//                text = r.toString()
                callback(r)
            } catch (e: Exception) {
                text = "fail:${e.message ?: e.toString()}"
                println(e.message ?: e.toString())
            }
        }
    }
    // 获取验证码
    fun getCaptcha(cookie: String,callback: (result: ByteArray) -> Unit) {
        thread {
            val result: Call<ResponseBody> = service.doPost(get_captcha_url, mapOf("Cookie" to cookie))
            try {
                val response = result.execute()
                val r = response.body()?.bytes() ?: ByteArray(0)
//                text = r.toString()
                callback(r)
            } catch (e: Exception) {
                text = "fail:${e.message ?: e.toString()}"
                println(e.message ?: e.toString())
            }
        }
    }
    // 登录
    fun login(username: String, password: String, execution: String, cookie: String, captcha: String = ""
              ,callback: (result: Boolean) -> Unit ) {
        thread {
            val requestBody = FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("execution", execution)
                .add("captcha", captcha)
                .add("_eventId", "submit")
                .add("cllt", "userNameLogin")
                .add("dllt", "generalLogin")
                .add("lt", "")
                .add("rememberMe", "true")
                .build()
            val request = Request.Builder()
                .url(login_url)
                .header("Cookie", cookie)
                .post(requestBody)
                .build()
            val result: Call<ResponseBody> = service.doPost(request.url.toString())
            try {
                val response = result.execute()
                val body = response.body()?.string() ?: ""
                val c = response.headers()["Set-Cookie"]
                callback(response.code() == 200 && !body.contains("帐号登录或动态码登录"))
            } catch (e: Exception) {
                text = "fail:${e.message ?: e.toString()}"
                println(e.message ?: e.toString())
            }
        }
    }
    fun getSchedule(cookie: String, term: String = ""){
        thread {
            // 初始化
            service.doPost(schedule_init_url,mapOf("Cookie" to cookie))
            service.doPost(schedule_lang_url,mapOf("Cookie" to cookie))
            // 获取学期
            var r = service.doPost(schedule_all_terms_url, mapOf("Cookie" to cookie))
            var response:Response<ResponseBody>
            var selectedTerm = term
            if (term.isBlank()) {
                r = service.doPost(schedule_now_term_url, mapOf("Cookie" to cookie))

                response = r.execute()
                val jsonString = response.body()?.string()

                val jsonParser = JsonParser()
                val jsonElement = jsonParser.parse(jsonString)
                if (jsonElement.isJsonObject)
                    println("jsonElement.isJsonObject")
                println("jsonString:${jsonString}")


                val jsonObject = Gson().fromJson(jsonString, JsonObject::class.java)
                selectedTerm = jsonObject.getAsJsonObject("datas")
                    .getAsJsonObject("dqxnxq")
                    .getAsJsonObject("rows")
                    .get("DM")
                    .asString
            }
            r = service.doPost(schedule_date_url, mapOf("Cookie" to cookie), mapOf("requestParamStr" to "{\"XNXQDM\":\"$selectedTerm\",\"ZC\":\"1\"}"))
            response = r.execute()
            // 获取初始日期
            var firstDay = LocalDate.MIN
            var jsonObject = Gson().fromJson(response.body()?.charStream(), JsonObject::class.java)
            val dataArray = jsonObject.getAsJsonArray("data")
            for (i in dataArray) {
                if (i.asJsonObject["XQ"].asInt == 1) {
                    firstDay = LocalDate.parse(i.asJsonObject["RQ"].asString)
                    break
                }
            }
            // 获取学期课表
            r = service.doPost(schedule_url, mapOf("Cookie" to cookie), mapOf("XNXQDM" to selectedTerm))
            response = r.execute()
            jsonObject = Gson().fromJson(response.body()?.charStream(), JsonObject::class.java)
            //    val scheduleList = Gson().fromJson(jsonObject
            //        .getAsJsonObject("datas")
            //        .getAsJsonArray("cxxszhxqkb")
            //        .asJsonObject
            //        .getAsJsonArray("rows"), Array<ScheduleData>::class.java)
            val scheduleList = jsonObject
                .getAsJsonObject("datas")
                .getAsJsonObject("cxxszhxqkb")
                .getAsJsonArray("rows")

            val dic =  mapOf(
                "term" to selectedTerm,
                "first_day" to firstDay,
                "data" to scheduleList
            )
            //    firstDay = LocalDate.parse(dic["first_day"], DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            var classCt = 0
            var timeCt = 0
            for (i  in scheduleList) {
                val schedule = i.asJsonObject
                val weekArray = schedule["SKZC"].asJsonArray
                weekArray.forEachIndexed() { index,item ->
                    if (item.toString().toInt() > 0) {
                        //                val event = VEvent()
                        ////                event.properties = XPropertyList()
                        //                event.properties.add(Uid(UUID.randomUUID().toString()))
                        //                event.properties.add(XProperty("SUMMARY", schedule["KCM"].toString()))
                        //                event.properties.add(XProperty("LOCATION", schedule["JASMC"].toString()))
                        //                event.properties.add(XProperty("DESCRIPTION", "${schedule["JASMC"]} | ${schedule["SKJS"]} | ${schedule["YPSJDD"]}"))
                        //                event.dateStart = DateTime(getTime(firstDay, index + 1, schedule["SKXQ"].toString().toInt(), schedule["KSJC"].toString().toInt(), 0))
                        //                event.dateEnd = DateTime(getTime(firstDay, index + 1, schedule["SKXQ"].toString().toInt(), schedule["JSJC"].toString().toInt(), 1))
                        //                cal.components.add(event)
                        print(schedule["KCM"].toString())

                        classCt += 1
                        timeCt += 45 * (schedule["JSJC"].toString().toInt() - schedule["KSJC"].toString().toInt() + 1)
                    }
                }
            }
        }
    }
    val userName = "1120211701"
    var password = "chenyuhao123."
    var execution = ""
    var salt: String
    var cookie: String
    var isNeedCaptcha :Boolean
    var captcha: ByteArray
    val assetManager=LocalContext.current.assets
    initLogin(){
        salt = it["salt"]!!
        execution = it["execution"]!!
        cookie = it["cookie"]!!

        password = encryptPassword(assetManager,password,salt)
        needCaptcha(userName){isNeed->
            if (isNeed){
                isNeedCaptcha = true
                getCaptcha(cookie){
                    captcha = it
                    text =
                        "execution:${execution}," +
                                "cookie:${cookie}," +
                                "isNeed:${it}," +
                                "captcha:${captcha}"
                    login(userName,password, execution = execution, cookie = cookie,
                        captcha = captcha.toString()){sucess->
                        text = if (sucess){
                            "sucess!"
                        }else "fail...."
                    }
                }
            }else{
                isNeedCaptcha = false
                login(userName,password,execution,cookie){sucess->
                    text = if (sucess){
                        "sucess!"
                    }else "fail...."
                }
            }
//            getSchedule(cookie)
        }
    }

    Scaffold(
        // 定义头部
        topBar = {
            AnotherTopAppBar(){
                //  返回
                navController.popBackStack()
            }
        }) {padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = text, fontWeight = FontWeight.SemiBold)
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
        }
    )
}
fun encryptPassword(assetManager: AssetManager, pwd0:String, key:String): String {
    // 创建Rhino上下文
    // 创建Rhino上下文
    val rhino: Context = Context.enter()
    try {
        // 设置JavaScript代码执行的作用域
        val scope: Scriptable = rhino.initStandardObjects()
        // 加载.js文件中的JavaScript代码
        val inputStream: InputStream = assetManager.open("EncryptPassword.js")
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)

//        val lines: List<String> = bufferedReader.readLines()
//        val content: String = lines.joinToString(separator = "\n")
//        println(content)
//        val fileReader = FileReader("EncryptPassword.js")
        rhino.evaluateReader(scope, bufferedReader, "MyJavaScript", 1, null)
        bufferedReader.close()

        // 调用JavaScript函数
        val pwd0 = "password"
        val key = "1234567890123456"
        val result: Any = callMethod(scope, "encryptPassword", arrayOf<Any>(pwd0, key))
        val encryptedPassword: String = Context.toString(result)
        Log.d("MyApp", "Encrypted password is: $encryptedPassword")
        return encryptedPassword
    } finally {
        // 退出Rhino上下文
        Context.exit()
    }
}