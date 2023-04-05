package com.example.timetable_application.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//Settings可以为timetable的settings，也可以是其他的
@Entity
data class Settings(
    @PrimaryKey val name: String,
    val value: String
)
