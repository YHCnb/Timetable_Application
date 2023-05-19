package com.example.timetable_application.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request

// 用于决定获取链接时返回哪种类型链接
var webvpn = false

// 检测webvpn是否可用
suspend fun checkWebvpn(){
    try {
        withContext(Dispatchers.IO){
            val client= HttpClient.client
            val request= Request.Builder()
                .url("http://webvpn.bit.edu.cn/")
                .build()
            client.newCall(request).execute()
            webvpn=true
        }
    } catch (e: Exception) {
        webvpn=false
    }
}

val login_url :String
    get() {
        return if (webvpn) "https://webvpn.bit.edu.cn/https/77726476706e69737468656265737421fcf84695297e6a596a468ca88d1b203b/authserver/login?service=https%3A%2F%2Fwebvpn.bit.edu.cn%2Flogin%3Fcas_login%3Dtrue"
        else "https://login.bit.edu.cn/authserver/login"
    }
val schedule_init_url: String
    get() {
        return if (webvpn) "https://webvpn.bit.edu.cn/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/funauthapp/api/getAppConfig/wdkbby-5959167891382285.do"
        else "http://jxzxehallapp.bit.edu.cn/jwapp/sys/funauthapp/api/getAppConfig/wdkbby-5959167891382285.do"
    }
val schedule_lang_url: String
    get() {
        return if (webvpn) "https://webvpn.bit.edu.cn/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/i18n.do?appName=wdkbby&EMAP_LANG=zh"
        else "http://jxzxehallapp.bit.edu.cn/jwapp/i18n.do?appName=wdkbby&EMAP_LANG=zh"
    }
val schedule_now_term_url: String
    get() {
        return if (webvpn) "https://webvpn.bit.edu.cn/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/modules/jshkcb/dqxnxq.do"
        else "http://jxzxehallapp.bit.edu.cn/jwapp/sys/wdkbby/modules/jshkcb/dqxnxq.do"
    }
val schedule_all_terms_url: String
    get() {
        return if (webvpn) "https://webvpn.bit.edu.cn/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/modules/jshkcb/xnxqcx.do"
        else "http://jxzxehallapp.bit.edu.cn/jwapp/sys/wdkbby/modules/jshkcb/xnxqcx.do"
    }
val schedule_date_url: String
    get() {
        return if (webvpn) "https://webvpn.bit.edu.cn/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/wdkbByController/cxzkbrq.do"
        else "http://jxzxehallapp.bit.edu.cn/jwapp/sys/wdkbby/wdkbByController/cxzkbrq.do"
    }
val schedule_url: String
    get() {
        return if (webvpn) "https://webvpn.bit.edu.cn/http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/modules/xskcb/cxxszhxqkb.do"
        else "http://jxzxehallapp.bit.edu.cn/jwapp/sys/wdkbby/modules/xskcb/cxxszhxqkb.do"
    }