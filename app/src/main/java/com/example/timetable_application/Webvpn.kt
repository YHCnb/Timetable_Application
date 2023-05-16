package com.example.timetable_application

import com.example.timetable_application.MyWebService.Companion.service
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Call
import kotlin.concurrent.thread

//import com.google.gson.Gson
//import com.google.gson.JsonObject
//import net.fortuna.ical4j.model.DateTime
//import net.fortuna.ical4j.model.component.VEvent
//import net.fortuna.ical4j.model.property.ProdId
//import net.fortuna.ical4j.model.property.Uid
//import net.fortuna.ical4j.model.property.Version
//import net.fortuna.ical4j.model.property.XProperty
//import net.fortuna.ical4j.model.property.XPropertyList
//import okhttp3.*
//import org.json.JSONArray
//import org.json.JSONObject
//import org.jsoup.Jsoup
//import java.time.DayOfWeek
//import java.time.LocalDate
//import java.time.ZoneId
//import java.time.format.DateTimeFormatter
//import java.util.Base64
//import kotlin.random.Random
//import java.util.*
//import kotlin.math.roundToInt
//
//val base_url = "https://webvpn.bit.edu.cn"
//val login_url =
//    "$base_url/https/77726476706e69737468656265737421fcf84695297e6a596a468ca88d1b203b/authserver/login?service=https%3A%2F%2Fwebvpn.bit.edu.cn%2Flogin%3Fcas_login%3Dtrue"
//val need_captcha_url =
//    "$base_url/https/77726476706e69737468656265737421fcf84695297e6a596a468ca88d1b203b/authserver/checkNeedCaptcha.htl"
//val get_captcha_url =
//    "$base_url/https/77726476706e69737468656265737421fcf84695297e6a596a468ca88d1b203b/authserver/getCaptcha.htl"
//val account_info_url =
//    "$base_url/https/77726476706e69737468656265737421fcf84695297e6a596a468ca88d1b203b/authserver/login"
//val student_info_init_url =
//    "$base_url/http/77726476706e69737468656265737421e3e354d225397c1e7b0c9ce29b5b/xsfw/sys/swpubapp/indexmenu/getAppConfig.do?appId=4585275700341858&appName=jbxxapp"
//val student_info_url =
//    "$base_url/http/77726476706e69737468656265737421e3e354d225397c1e7b0c9ce29b5b/xsfw/sys/jbxxapp/modules/infoStudent/getStuBatchInfo.do"
//val score_base_url =
//    "$base_url/https/77726476706e69737468656265737421fae04c8f69326144300d8db9d6562d"
//val score_url = "$score_base_url/jsxsd/kscj/cjcx_list"
//val report_login_url =
//    "$base_url/https/77726476706e69737468656265737421fae042d225397c1e7b0c9ce29b5b/cjd/Account/ExternalLogin"
//val report_url =
//    "$base_url/https/77726476706e69737468656265737421fae042d225397c1e7b0c9ce29b5b/cjd/ScoreReport2/Index?GPA=1"
////课程表模块
//val schedule_init_url =
//    "$base_url/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/funauthapp/api/getAppConfig/wdkbby-5959167891382285.do"
//val schedule_lang_url =
//    "$base_url/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/i18n.do?appName=wdkbby&EMAP_LANG=zh"
//val schedule_now_term_url =
//    "$base_url/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/modules/jshkcb/dqxnxq.do"
//val schedule_all_terms_url =
//    "$base_url/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/modules/jshkcb/xnxqcx.do"
//val schedule_url =
//    "$base_url/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/modules/xskcb/cxxszhxqkb.do"
//val schedule_date_url=
//    "$base_url/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/wdkbByController/cxzkbrq.do"
//
//
//fun my_redirection(url: String, head: Map<String, String> = mapOf(), data: Map<String, String> = mapOf()): Response {
//    val client = OkHttpClient.Builder().build()
//    var requestBuilder = Request.Builder().url(url)
//    head.forEach { (key, value) ->
//        requestBuilder = requestBuilder.header(key, value)
//    }
//    val requestBody = FormBody.Builder()
//    data.forEach { (key, value) ->
//        requestBody.add(key, value)
//    }
//    val request = requestBuilder.post(requestBody.build()).build()
//    var response = client.newCall(request).execute()
//    while (response.code() == 302) {
//        val newUrl = response.header("Location")
//        var finalUrl = newUrl ?: ""
//        if (finalUrl.startsWith("/")) {
//            val baseUrl = HttpUrl.parse(url)?.newBuilder()?.build()?.let {
//                it.scheme() + "://" + it.host() + ":" + it.port()
//            } ?: ""
//            finalUrl = baseUrl + finalUrl
//        }
//        val newRequestBuilder = Request.Builder().url(finalUrl)
//        head.forEach { (key, value) ->
//            newRequestBuilder.header(key, value)
//        }
//        val newRequest = newRequestBuilder.build()
//        response = client.newCall(newRequest).execute()
//    }
//    return response
//}
//
//// 获取身份认证初始信息
//fun initLogin(): Map<String, String?> {
//    val response = redirection(login_url)
//    val soup = response.body()?.string()?.let { Jsoup.parse(it) }
//    val pwdFrom = soup!!.getElementById("pwdFromId")!!
//    val salt = pwdFrom.getElementById("pwdEncryptSalt")?.attr("value")
//    val execution = pwdFrom.getElementById("execution")?.attr("value")
//    val cookie = response.header("Set-Cookie")
//    return mapOf("salt" to salt, "execution" to execution, "cookie" to cookie)
//}
//
//// 查询是否需要验证码
//fun needCaptcha(sid: String): Boolean {
//    val response = redirection("$need_captcha_url?username=$sid")
//    val json = response.body()?.string() ?: ""
//    return json.contains("\"isNeed\":true")
//}
//
//// 获取验证码
//fun getCaptcha(cookie: String): ByteArray {
//    val response = redirection(get_captcha_url, mapOf("Cookie" to cookie))
//    return response.body()?.bytes() ?: ByteArray(0)
//}
//
//// 登录
//fun login(username: String, password: String, execution: String, cookie: String, captcha: String = ""): Boolean {
//    val requestBody = FormBody.Builder()
//        .add("username", username)
//        .add("password", password)
//        .add("execution", execution)
//        .add("captcha", captcha)
//        .add("_eventId", "submit")
//        .add("cllt", "userNameLogin")
//        .add("dllt", "generalLogin")
//        .add("lt", "")
//        .add("rememberMe", "true")
//        .build()
//    val request = Request.Builder()
//        .url(login_url)
//        .header("Cookie", cookie)
//        .post(requestBody)
//        .build()
//    val response = redirection(request.url().toString())
//    val body = response.body()?.string() ?: ""
//    return response.code() == 200 && !body.contains("帐号登录或动态码登录")
//}
////由于没有给出redirection函数的实现，我们假设它的返回值是一个响应对象，其具有一个json()方法，
////该方法返回JSON响应的字典形式。另外，由于Kotlin 1.5开始支持多行字符串字面量，因此我们使用三重引号(""")来创建包含换行符的JSON请求字符串。
//fun getSchedule(cookie: String, term: String = ""): Map<String, String> {
//    // 初始化
//    redirection(schedule_init_url, mapOf("Cookie" to cookie))
//    redirection(schedule_lang_url, mapOf("Cookie" to cookie))
//
//    // 获取学期
//    var r = redirection(schedule_all_terms_url, mapOf("Cookie" to cookie))
//    var selectedTerm = term
//    if (term.isBlank()) {
//        r = redirection(schedule_now_term_url, mapOf("Cookie" to cookie))
//        val jsonString = r.body()?.string()
//        val jsonObject = Gson().fromJson(jsonString, JsonObject::class.java)
//        selectedTerm = jsonObject.getAsJsonObject("datas")
//            .getAsJsonObject("dqxnxq")
//            .getAsJsonObject("rows")
//            .get("DM")
//            .asString
//    }
//    r = redirection(schedule_date_url, mapOf("Cookie" to cookie), mapOf("requestParamStr" to "{\"XNXQDM\":\"$selectedTerm\",\"ZC\":\"1\"}"))
//    // 获取初始日期
//    var firstDay = LocalDate.MIN
//    var jsonObject = Gson().fromJson(r.body()?.charStream(), JsonObject::class.java)
//    val dataArray = jsonObject.getAsJsonArray("data")
//    for (i in dataArray) {
//        if (i.asJsonObject["XQ"].asInt == 1) {
//            firstDay = LocalDate.parse(i.asJsonObject["RQ"].asString)
//            break
//        }
//    }
//    // 获取学期课表
//    r = redirection(schedule_url, mapOf("Cookie" to cookie), mapOf("XNXQDM" to selectedTerm))
//    jsonObject = Gson().fromJson(r.body()?.charStream(), JsonObject::class.java)
////    val scheduleList = Gson().fromJson(jsonObject
////        .getAsJsonObject("datas")
////        .getAsJsonArray("cxxszhxqkb")
////        .asJsonObject
////        .getAsJsonArray("rows"), Array<ScheduleData>::class.java)
//    val scheduleList = jsonObject
//        .getAsJsonObject("datas")
//        .getAsJsonObject("cxxszhxqkb")
//        .getAsJsonArray("rows")
//
//    val dic =  mapOf(
//        "term" to selectedTerm,
//        "first_day" to firstDay,
//        "data" to scheduleList
//    )
////    firstDay = LocalDate.parse(dic["first_day"], DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//    var classCt = 0
//    var timeCt = 0
//    for (i  in scheduleList) {
//        val schedule = i.asJsonObject
//        val weekArray = schedule["SKZC"].asJsonArray
//        weekArray.forEachIndexed() { index,item ->
//            if (item.toString().toInt() > 0) {
////                val event = VEvent()
//////                event.properties = XPropertyList()
////                event.properties.add(Uid(UUID.randomUUID().toString()))
////                event.properties.add(XProperty("SUMMARY", schedule["KCM"].toString()))
////                event.properties.add(XProperty("LOCATION", schedule["JASMC"].toString()))
////                event.properties.add(XProperty("DESCRIPTION", "${schedule["JASMC"]} | ${schedule["SKJS"]} | ${schedule["YPSJDD"]}"))
////                event.dateStart = DateTime(getTime(firstDay, index + 1, schedule["SKXQ"].toString().toInt(), schedule["KSJC"].toString().toInt(), 0))
////                event.dateEnd = DateTime(getTime(firstDay, index + 1, schedule["SKXQ"].toString().toInt(), schedule["JSJC"].toString().toInt(), 1))
////                cal.components.add(event)
//                print(schedule["KCM"].toString())
//
//                classCt += 1
//                timeCt += 45 * (schedule["JSJC"].toString().toInt() - schedule["KSJC"].toString().toInt() + 1)
//            }
//        }
//    }
//
//    return mapOf("msg" to "一共添加了${dic["term"]}学期的${classCt}节课，合计坐牢时间${(timeCt / 60.0).roundToInt()}小时（雾")
//}
//
//private fun getTime(firstDay: LocalDate, week: Int, dayOfWeek: Int, startHour: Int, minute: Int): Date {
//    val date = firstDay.with(DayOfWeek.of(dayOfWeek)).plusWeeks((week - 1).toLong())
//    val time = date.atTime(startHour, minute)
//    return Date.from(time.atZone(ZoneId.systemDefault()).toInstant())
//}
//
//
//// 统一身份认证验证初始化
//fun webvpnVerifyInit(sid: String): MutableMap<String, String?> {
//    val dic = initLogin().toMutableMap()
//    if (needCaptcha(sid)) {
//        val img = getCaptcha(dic["cookie"] as String)
//        dic["captcha"] = Base64.getEncoder().encodeToString(img)
//    } else {
//        dic["captcha"] = false.toString()
//    }
//    return dic
//}
//
//fun main() {
//    data = request.get_json()
//    username = data.get('username', '')
//    password = data.get('password', '')
//    execution = data.get('execution', '')
//    cookie = data.get('cookie', '')
//    captcha = data.get('captcha', '')
//    login(username, password, execution, cookie, captcha)
//
//    val cookie = request?.getParameter("cookie") ?: ""
//    val term = request?.getParameter("term") ?: ""
//
//    val out = getSchedule(cookie,term)
//}



