package com.example.timetable_application

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import de.prosiebensat1digital.oasisjsbridge.JsBridge
import de.prosiebensat1digital.oasisjsbridge.JsBridgeConfig
import de.prosiebensat1digital.oasisjsbridge.JsValue
import de.prosiebensat1digital.oasisjsbridge.NativeToJsInterface
import kotlin.concurrent.thread

interface JsApi : NativeToJsInterface {
    fun getAesString(data:String, key0:String, iv0:String): String
    fun encryptAES(data:String, aesKey:String): String
    fun encryptPassword(pwd0:String, key:String): String
    fun randomString(len: Int): String
}

suspend fun encryptPassword(pwd0: String, key: String?, context:Context): String {
    val jsBridge = JsBridge(JsBridgeConfig.bareConfig(),context)
    return ""
}
@Composable
fun Main() {
    val context = LocalContext.current
    LaunchedEffect(key1 = "mykey"){
        encryptPassword("112233","112233",context)
    }
}

