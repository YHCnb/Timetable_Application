package com.example.timetable_application.utils

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.Request
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//此思路由范大神提供，大神的网站请看--->https://github.com/flwfdd/BIT101/tree/main
private data class NowTermResponseRoot(
    var datas: NowTermResponseDatas
)

private data class NowTermResponseDatas(
    var dqxnxq: NowTermResponseDqxnxq
)

private data class NowTermResponseDqxnxq(
    var rows: List<TermResponseItem>
)

data class TermResponseItem(
    var DM: String,
)

private data class DateResponseRoot(
    var data: List<DateResponseItem>
)

private data class DateResponseItem(
    var XQ: Int,
    var RQ: String,
)

private data class CourseResponseRoot(
    var datas: CourseResponseDatas
)

private data class CourseResponseDatas(
    var cxxszhxqkb: CourseResponseCxxszhxqkb
)

private data class CourseResponseCxxszhxqkb(
    var rows: List<CourseResponseItem>
)

data class CourseResponseItem(
    var XNXQDM: String?, // 学年学期
    var KCM: String?, // 课程名
    var SKJS: String?, // 授课教师 逗号分隔
    var JASMC: String?, // 教室
    var YPSJDD: String?, // 上课时空描述
    var SKZC: String?, // 上课周次 一个01串 1表示上课 0表示不上课
    var SKXQ: Int?, // 星期几
    var KSJC: Int?, // 开始节次
    var JSJC: Int?, // 结束节次
    var XXXQMC: String?, // 校区
    var KCH: String?, // 课程号
    var XF: Int?, // 学分
    var XS: Int?, // 学时
    var KCXZDM_DISPLAY: String?, // 课程性质 必修选修什么的
    var KCLBDM_DISPLAY: String?, // 课程类别 文化课实践课什么的
    var KKDWDM_DISPLAY: String?, // 开课单位
)

data class CourseScheduleResponse(
    val term: String,
    var firstDay: LocalDate,
    var courseList: List<CourseResponseItem>
)

suspend fun getCourseSchedule(): CourseScheduleResponse? {
    var term = ""
    try {
        return withContext(Dispatchers.IO) {
            val client = HttpClient.client
            val initRequest = Request.Builder()
                .url(schedule_init_url)
                .build()
            client.newCall(initRequest).execute().close()

            val langRequest = Request.Builder()
                .url(schedule_lang_url)
                .build()
            client.newCall(langRequest).execute().close()

            val termRequest = Request.Builder()
                .url(schedule_now_term_url)
                .build()
            client.newCall(termRequest).execute().use { response ->
                val res =
                    Gson().fromJson(response.body?.string(), NowTermResponseRoot::class.java)
                term = res.datas.dqxnxq.rows[0].DM
            }

            var firstDay: LocalDate? = null
            val dateBody = FormBody.Builder()
                .add("requestParamStr", "{\"XNXQDM\":\"$term\",\"ZC\":\"1\"}")
                .build()
            val dateRequest = Request.Builder()
                .url(schedule_date_url)
                .post(dateBody)
                .build()
            client.newCall(dateRequest).execute().use { response ->
                val res = Gson().fromJson(response.body?.string(), DateResponseRoot::class.java)
                for (item in res.data) {
                    if (item.XQ == 1) {
                        firstDay =
                            LocalDate.parse(item.RQ, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        break
                    }
                }
            }
            if (firstDay == null) {
                return@withContext null
            }

            val body = FormBody.Builder()
                .add("XNXQDM", term)
                .build()
            val scheduleRequest = Request.Builder()
                .url(schedule_url)
                .post(body)
                .build()
            client.newCall(scheduleRequest).execute().use { response ->
                val res = Gson().fromJson(response.body?.string(), CourseResponseRoot::class.java)
                println(res.datas.cxxszhxqkb.rows)
                return@withContext CourseScheduleResponse(
                    term,
                    firstDay!!,
                    res.datas.cxxszhxqkb.rows
                )
            }
        }
    } catch (e: Exception) {
        Log.e("SchoolSchedule", "Get Course Schedule Error $e")
        return null
    }
}

