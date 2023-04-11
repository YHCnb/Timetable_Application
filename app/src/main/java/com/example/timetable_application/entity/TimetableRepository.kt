package com.example.timetable_application.entity

import com.example.timetable_application.db.TimetableDatabase
import kotlinx.coroutines.flow.first

// 定义一个接口，表示获取课表的功能
interface TimetableRepository {
    suspend fun getTimetable(): Timetable
}

// 实现这个接口，使用数据库查询获取课表
class DatabaseTimetableRepository(private val db: TimetableDatabase) {
    private val timetableDao = db.timetableDao()
    //获取默认课表
    suspend fun getTimetable(): Timetable {

        //获取默认课表名
        val defaultTimetableName = timetableDao.getSettingByName("default_timetable").value
        //返回默认课表
        return timetableDao.getTimetableByName(defaultTimetableName)
    }
    //获取所有课表
    suspend fun getAllTimetable(): List<Timetable> {
        return timetableDao.getAllTimetable().first()
    }
    //设置默认课表名
    suspend fun setDefaultTimetable(name: String) {
        // update the default timetable name in the settings table
        timetableDao.updateSettingByName("default_timetable", name)
    }
    //为数据库添加timetable
    suspend fun addTimetable(timetable: Timetable) {
        timetableDao.insertTimetable(timetable)
    }
    //获取tb
    suspend fun getTimetableByName(name:String): Timetable {
        return timetableDao.getTimetableByName(name)
    }
    //删除tb
    suspend fun deleteTimetable(name:String){
        timetableDao.deleteTimetable(name)
    }

    //timetable基本属性修改
    suspend fun editName(name:String,tartgetName:String){
        val tb = timetableDao.getTimetableByName(name)
        timetableDao.deleteTimetable(name)
        tb.name=tartgetName
        timetableDao.insertTimetable(tb)
        if(name == timetableDao.getSettingByName("default_timetable").value){//如果该默认表名字，则改setting
            timetableDao.updateSettingByName("default_timetable",tartgetName)
        }
    }
    suspend fun editStartTime(name:String,startTime:String){
        val tb = timetableDao.getTimetableByName(name)
        tb.startTime=startTime
        timetableDao.updateTimetable(tb)
    }
    suspend fun editCurWeek(name:String,curWeek:Int){
        val tb = timetableDao.getTimetableByName(name)
        tb.curWeek=curWeek
        timetableDao.updateTimetable(tb)
    }
    suspend fun editCoursesPerDay(name:String,coursesPerDay:Int){
        val tb = timetableDao.getTimetableByName(name)
        tb.coursesPerDay=coursesPerDay
        timetableDao.updateTimetable(tb)
    }
    suspend fun editWeeksOfTerm(name:String,weeksOfTerm:Int){
        val tb = timetableDao.getTimetableByName(name)
        tb.weeksOfTerm=weeksOfTerm
        timetableDao.updateTimetable(tb)
    }

    //course基本属性修改
    suspend fun getCourseByName(tbName:String,courseName:String): Course {
        val tb = timetableDao.getTimetableByName(tbName)
        return tb.courseMap[courseName]!!
    }
    suspend fun deleteCourse(tbName:String,courseName:String) {
        val tb = timetableDao.getTimetableByName(tbName)
        tb.courseMap.remove(courseName)
    }
    suspend fun getAllCourse(tbName:String): List<Course> {
        val tb = timetableDao.getTimetableByName(tbName)
        return tb.courseMap.values.toList()
    }
    suspend fun updateCourse(tbName:String,courseName:String,newCourse: Course){
        //需要保证courseName对应的course就是newCourse要替换的对象
        val tb = timetableDao.getTimetableByName(tbName)
        if(courseName == newCourse.name){
            tb.courseMap[courseName] = newCourse
        }else{
            tb.courseMap.remove(courseName)
            tb.courseMap[newCourse.name] = newCourse
        }
        timetableDao.updateTimetable(tb)
    }
    suspend fun addCourse(tbName:String,course: Course){
        val tb = timetableDao.getTimetableByName(tbName)
        tb.courseMap[course.name] = course
        timetableDao.updateTimetable(tb)
    }
    suspend fun addCourseTime(tbName:String,name:String,courseTime: CourseTime){
        val tb = timetableDao.getTimetableByName(tbName)
        tb.courseMap[name]?.time?.add(courseTime)
        timetableDao.updateTimetable(tb)
    }
    suspend fun deleteCourseTime(tbName:String,name:String,index:Int){
        val tb = timetableDao.getTimetableByName(tbName)
        tb.courseMap[name]?.time?.removeAt(index)
        timetableDao.updateTimetable(tb)
    }
    suspend fun editCourseTime(tbName:String,name:String,index:Int,courseTime: CourseTime){
        val tb = timetableDao.getTimetableByName(tbName)
        tb.courseMap[name]?.time?.set(index,courseTime)
        timetableDao.updateTimetable(tb)
    }
    suspend fun editCourseColor(tbName:String,name:String,targetColor:String){
        val tb = timetableDao.getTimetableByName(tbName)
        tb.courseMap[name]?.color=targetColor
        timetableDao.updateTimetable(tb)
    }
    suspend fun editCourseName(tbName:String,name:String,targetName:String){
        val tb = timetableDao.getTimetableByName(tbName)
        val targetCourse = tb.courseMap[name]
        if (targetCourse != null) {
            targetCourse.name=targetName
            tb.courseMap.remove(name)
            tb.courseMap[targetName] = targetCourse
        }
        timetableDao.updateTimetable(tb)
    }
}

