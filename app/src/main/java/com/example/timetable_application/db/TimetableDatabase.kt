package com.example.timetable_application.db

import androidx.room.*
import com.example.timetable_application.entity.*
import kotlinx.coroutines.flow.Flow

//定义数据存储对象
@Dao
interface TimetableDao{
    //timetable
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetable(timetable: Timetable)
    @Update
    suspend fun updateTimetable(timetable: Timetable)
    @Query("delete FROM timetable WHERE name = :name")
    suspend fun deleteTimetable(name: String)
    @Query("select * from timetable")
    fun getAllTimetable(): Flow<List<Timetable>>
    @Query("select * from timetable where name=:name")
    suspend fun getTimetableByName(name:String): Timetable
    //setting
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: Settings)
    @Query("UPDATE settings SET value = :value WHERE name = :name")
    suspend fun updateSettingByName(name: String, value: String)
    @Query("SELECT * FROM settings WHERE name = :name")
    suspend fun getSettingByName(name: String): Settings
    //OneClassTime
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneClassTime(oneClassTime: OneClassTime)
}

@Database(
    entities = [Timetable::class, Settings::class, OneClassTime::class],  //表明本数据库有几张表
    version = 2  //表示当前数据库的版本号
)
@TypeConverters(Converters::class)
abstract class TimetableDatabase:RoomDatabase(){
    //引用存取特定表的数据存储对象（Dao）
    abstract fun timetableDao(): TimetableDao
}