package com.example.timetable_application.utils

import android.util.Log
import com.evgenii.jsevaluator.JsEvaluator
import com.evgenii.jsevaluator.interfaces.JsCallback
import com.example.timetable_application.MyApp
import kotlinx.coroutines.*
import okhttp3.FormBody
import okhttp3.Request
import org.jsoup.Jsoup
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private suspend fun encryptPassword(password: String, salt: String): String? {
    return withContext(Dispatchers.Main) {
        return@withContext suspendCoroutine { it ->
            val jsSrc = MyApp.context.assets.open("EncryptPassword.js")
                .bufferedReader()
                .use { it.readText() }
            val jsEvaluator = JsEvaluator(MyApp.context)
            jsEvaluator.callFunction(jsSrc, object : JsCallback {
                override fun onResult(value: String?) {
                    it.resume(value)
                }
                override fun onError(errorMessage: String?) {
                    Log.e("SchoolLogin",errorMessage.toString())
                    it.resume(null)
                }
            }, "encryptPassword", password, salt)
        }
    }
}

// 登陆
suspend fun login(username: String, password: String): Boolean {
    try {
        withContext(Dispatchers.IO) {
            val client = HttpClient.client

            // 登录初始化
            val initLoginRequest = Request.Builder()
                .url(login_url)
                .build()
            var cryptPassword = ""
            var execution = ""
            client.newCall(initLoginRequest).execute().use { response ->
                val html =
                    response.body?.string() ?: throw Exception("get login init response error")
                val doc = Jsoup.parse(html)
                val form = doc.select("#pwdFromId")
                val salt = form.select("#pwdEncryptSalt").attr("value")
                execution = form.select("#execution").attr("value")
                cryptPassword =
                    encryptPassword(password, salt) ?: throw Exception("encrypt password error")
            }

            // 登录
            val body = FormBody.Builder()
                .add("username", username)
                .add("password", cryptPassword)
                .add("execution", execution)
                .add("captcha", "")
                .add("_eventId", "submit")
                .add("cllt", "userNameLogin")
                .add("dllt", "generalLogin")
                .add("lt", "")
                .add("rememberMe", "true")
                .build()
            val loginRequest = Request.Builder()
                .url(login_url)
                .post(body)
                .build()
            client.newCall(loginRequest).execute().use { response ->
                val html =
                    response.body?.string() ?: throw Exception("get login response error")
                if (html.indexOf("帐号登录或动态码登录") != -1) {
                    throw Exception("login error")
                }
            }
        }
    } catch (e: Exception) {
        Log.e("SchoolLogin", "Login Error $e")
        return false
    }
    return true
}
// 检查是否仍为登录状态
suspend fun checkLogin(): Boolean {
    try {
        withContext(Dispatchers.IO) {
            val client = HttpClient.client
            // 登录初始化
            val initLoginRequest = Request.Builder()
                .url(login_url)
                .build()
            client.newCall(initLoginRequest).execute().use { response ->
                val html =
                    response.body?.string() ?: throw Exception("check login response error")
                if (html.indexOf("帐号登录或动态码登录") != -1) {
                    throw Exception("username or password error")
                }
            }
        }
    } catch (e: Exception) {
        Log.e("SchoolLogin", "Login Error $e")
        return false
    }
    return true
}

// 退出登录
fun logout() {
    HttpClient.cookieManager.cookieStore.removeAll()
}
