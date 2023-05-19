package com.example.timetable_application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApp: Application(){
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
//        val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = "settings")
    }
    override fun onCreate() {
        super.onCreate()
        context =applicationContext
    }
}


