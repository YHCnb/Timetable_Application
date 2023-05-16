package com.example.timetable_application

import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MyWebService {
    companion object {
        //要访问的RESTful Service的基地址
        //注意URL的值: 总是以/结尾， 不要以/开头
//        var baseUrl = "https://jinxuliang.com/MyWebApiServer/"
        val service: MyWebService by lazy {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    var request = chain.request()
                    var response = chain.proceed(request)
                    while (response.code == 302) {
                        val location = response.header("Location")
                            ?: throw RuntimeException("Missing location header")
                        val redirectUrl = location.toHttpUrlOrNull()
                            ?: throw RuntimeException("Invalid redirect location: $location")
                        request = request.newBuilder()
                            .url(redirectUrl)
                            .build()
                        response = chain.proceed(request)
                    }
                    response
                }
                .build()
            //实例化Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client) // 设置 HTTP 客户端
                //使用Gson转换器，将json字符串反序列化为JavaBean或反之
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            //构建用于访问REST服务的retrofit实例
            retrofit.create(MyWebService::class.java)
        }
    }

    //自动反序列化为Java对象
    @GET("api/myservice")
    fun getValue(): Call<ResponseBody>

    @GET
    fun doGet(
        @Url url: String,
        @HeaderMap headers: Map<String, String> = emptyMap()
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST
    fun doPost(
        @Url url: String,
        @FieldMap fields: Map<String, String> = emptyMap(),
        @HeaderMap headers: Map<String, String> = emptyMap()
    ): Call<ResponseBody>

}
const val baseUrl = "https://webvpn.bit.edu.cn/"
const val login_url =
    "${baseUrl}https/77726476706e69737468656265737421fcf84695297e6a596a468ca88d1b203b/authserver/login?service=https%3A%2F%2Fwebvpn.bit.edu.cn%2Flogin%3Fcas_login%3Dtrue"
const val need_captcha_url =
    "${baseUrl}https/77726476706e69737468656265737421fcf84695297e6a596a468ca88d1b203b/authserver/checkNeedCaptcha.htl"
const val get_captcha_url =
    "${baseUrl}https/77726476706e69737468656265737421fcf84695297e6a596a468ca88d1b203b/authserver/getCaptcha.htl"
const val account_info_url =
    "https/77726476706e69737468656265737421fcf84695297e6a596a468ca88d1b203b/authserver/login"
const val student_info_init_url =
    "http/77726476706e69737468656265737421e3e354d225397c1e7b0c9ce29b5b/xsfw/sys/swpubapp/indexmenu/getAppConfig.do?appId=4585275700341858&appName=jbxxapp"
const val student_info_url =
    "http/77726476706e69737468656265737421e3e354d225397c1e7b0c9ce29b5b/xsfw/sys/jbxxapp/modules/infoStudent/getStuBatchInfo.do"
const val score_base_url =
    "https/77726476706e69737468656265737421fae04c8f69326144300d8db9d6562d"
const val score_url = "jsxsd/kscj/cjcx_list"
const val report_login_url =
    "https/77726476706e69737468656265737421fae042d225397c1e7b0c9ce29b5b/cjd/Account/ExternalLogin"
const val report_url =
    "https/77726476706e69737468656265737421fae042d225397c1e7b0c9ce29b5b/cjd/ScoreReport2/Index?GPA=1"
//课程表模块
const val schedule_init_url =
    "${baseUrl}http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/funauthapp/api/getAppConfig/wdkbby-5959167891382285.do"
const val schedule_lang_url =
    "${baseUrl}http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/i18n.do?appName=wdkbby&EMAP_LANG=zh"
const val schedule_now_term_url =
    "${baseUrl}http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/modules/jshkcb/dqxnxq.do"
const val schedule_all_terms_url =
    "${baseUrl}http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/modules/jshkcb/xnxqcx.do"
const val schedule_url =
    "${baseUrl}http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/modules/xskcb/cxxszhxqkb.do"
const val schedule_date_url=
    "${baseUrl}http/77726476706e69737468656265737421faef5b842238695c720999bcd6572a216b231105adc27d/jwapp/sys/wdkbby/wdkbByController/cxzkbrq.do"

