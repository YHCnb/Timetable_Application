package com.example.timetable_application.utils

import com.example.timetable_application.MyApp
import net.gotev.cookiestore.SharedPreferencesCookieStore
import net.gotev.cookiestore.okhttp.JavaNetCookieJar
import okhttp3.OkHttpClient
import java.net.CookieManager
import java.net.CookiePolicy

class HttpClient {
    companion object {
        val cookieManager by lazy {
            CookieManager(
                SharedPreferencesCookieStore(MyApp.context, "Cookie"),
                CookiePolicy.ACCEPT_ALL
            )
        }
        val client by lazy {
            OkHttpClient.Builder()
                .cookieJar(
                    JavaNetCookieJar(cookieManager)
                )
                .build()
        }
    }
}