package com.example.timetable_application

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Call
import kotlin.concurrent.thread

//fun initLogin() {
//    thread {
//        //Call对象封装了向Web Server端发出HTTP请求，并取回结果的功能
////            val result: Call<ResponseBody> = service.getValue()
//        val result: Call<ResponseBody> = MyWebService.service.doPost(login_url)
//        try {
//            val response = result.execute()
//            val soup = response.body()?.string()?.let { Jsoup.parse(it) }
//            val pwdFrom = soup!!.getElementById("pwdFromId")!!
//            val salt = pwdFrom.getElementById("pwdEncryptSalt")?.attr("value")
//            val execution = pwdFrom.getElementById("execution")?.attr("value")
//            val cookie = response.headers()["Set-Cookie"]
//            //提取出完整的从server端发回的Json字符串
//            text = "sucess:${
//                mapOf(
//                    "salt" to salt,
//                    "execution" to execution,
//                    "cookie" to cookie
//                ).toString()
//            }"
//        } catch (e: Exception) {
//            text = "fail:${e.message ?: e.toString()}"
//            println(e.message ?: e.toString())
//        }
//    }
//    return mapOf("salt" to salt, "execution" to execution, "cookie" to cookie)
//}