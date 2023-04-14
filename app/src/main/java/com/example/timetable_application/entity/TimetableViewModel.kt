package com.example.timetable_application.entity

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.example.timetable_application.db.DbHelper
import com.example.timetable_application.db.TimetableDatabase
import kotlinx.coroutines.launch

class TimetableViewModel(private val app:Application) : AndroidViewModel(app) {
    //创建数据库
    private val db: TimetableDatabase by lazy {
        Log.d("Room","TimetableDatabase is created")
        Room.databaseBuilder(
            app, TimetableDatabase::class.java,
            "timetable.db"
        )
            .createFromAsset("default_data.db")
            .build()
    }
    //timetableRepository
    private var timetableRepository: DatabaseTimetableRepository = DatabaseTimetableRepository(db)
    //课表元素
    private val _timetableName = MutableLiveData<String>()
    val timetableName: LiveData<String>
        get() = _timetableName
    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String>
        get() = _startTime
    private val _curWeek = MutableLiveData<Int>()
    val curWeek: LiveData<Int>
        get() = _curWeek
    private val _coursesPerDay = MutableLiveData<Int>()
    val coursesPerDay: LiveData<Int>
        get() = _coursesPerDay
    private val _weeksOfTerm = MutableLiveData<Int>()
    val weeksOfTerm: LiveData<Int>
        get() = _weeksOfTerm
    private val _courseMap = MutableLiveData<MutableMap<String, Course>>()
    val courseMap: LiveData<MutableMap<String, Course>>
        get() = _courseMap
    private val _timetableList = MutableLiveData< List<Timetable> >()
    val timetableList: LiveData< List<Timetable> >
        get() = _timetableList
    private val _defaultTimetableName = MutableLiveData< String >()
    val defaultTimetableName: LiveData< String >
        get() = _defaultTimetableName

    init {
        viewModelScope.launch {
            changeTimetable()
            _defaultTimetableName.value = timetableRepository.getDefaultTimetableName()
        }
    }
    //获取所有课表
    fun getTimetableList() {
        viewModelScope.launch {
            _timetableList.value = timetableRepository.getAllTimetable()
        }
    }
    //设置默认课表
    fun setDefaultTimetable(name: String){
        viewModelScope.launch {
            //LiveData 只会在其值被设置时才会触发更新，而不会在其值的属性被修改时触发更新。
            // 所以，如果只是修改了 _timetable.value 的 name 属性，
            // 而没有重新设置 _timetable.value，那么 Compose 中的状态变量就不会收到通知，并且界面也不会刷新。
            _defaultTimetableName.value = name
            timetableRepository.setDefaultTimetable(name)
        }
    }
    //添加课表
    fun addTimetable(timetable: Timetable){
        viewModelScope.launch {
            timetableRepository.addTimetable(timetable)
            getTimetableList()
        }
    }
    //改变当前的timetable,name为“”则为默认课表
    fun changeTimetable(name: String="") {
        viewModelScope.launch {
            if( _timetableName.value==null ||(name==""&&_timetableName.value!=_defaultTimetableName.value)
                ||(name!=""&&_timetableName.value!=name) ){
                val timetable = if(name==""){timetableRepository.getTimetable()}
                                else {timetableRepository.getTimetableByName(name)}
                _timetableName.value = timetable.name
                _startTime.value = timetable.startTime
                _curWeek.value = timetable.curWeek
                _coursesPerDay.value = timetable.coursesPerDay
                _weeksOfTerm.value = timetable.weeksOfTerm
                _courseMap.value = timetable.courseMap
                _timetableList.value = timetableRepository.getAllTimetable()
            }
        }
    }
    //删除课表
    fun deleteTimetable(name:String){//无法删除默认课表
        viewModelScope.launch {
            timetableRepository.deleteTimetable(name)
            getTimetableList()
        }
    }
    //课表属性修改
    fun editName(targetName: String){
        viewModelScope.launch {
            _timetableName.value = targetName
            timetableRepository.editName(_timetableName.value!!,targetName)
        }
    }
    fun editStartTime(startTime: String){
        viewModelScope.launch {
            _startTime.value = startTime
            timetableRepository.editStartTime(_timetableName.value!!,startTime)
        }
    }
    fun editCurWeek(curWeek: Int){
        viewModelScope.launch {
            _curWeek.value = curWeek
            timetableRepository.editCurWeek(_timetableName.value!!,curWeek)
        }
    }
    fun editCoursesPerDay(coursesPerDay: Int){
        viewModelScope.launch {
            _coursesPerDay.value = coursesPerDay
            timetableRepository.editCoursesPerDay(_timetableName.value!!,coursesPerDay)
        }
    }
    fun editWeeksOfTerm(weeksOfTerm: Int){
        viewModelScope.launch {
            _weeksOfTerm.value = weeksOfTerm
            timetableRepository.editWeeksOfTerm(_timetableName.value!!,weeksOfTerm)
            if (_curWeek.value!! >weeksOfTerm){
                _curWeek.value = weeksOfTerm
                timetableRepository.editCurWeek(_timetableName.value!!,weeksOfTerm)
            }
        }
    }
    //课程修改
    fun updateCourse(courseName:String,newCourse: Course){
        viewModelScope.launch {
            _courseMap.value?.replace(courseName, newCourse)
            timetableRepository.updateCourse(_timetableName.value!!,courseName,newCourse)
        }
    }
    fun deleteCourse(courseName:String){
        viewModelScope.launch {
            _courseMap.value!!.remove(courseName)
            timetableRepository.deleteCourse(_timetableName.value!!,courseName)
        }
    }
    fun addCourse(course: Course){
        viewModelScope.launch {
            _courseMap.value?.set(course.name,course)
            timetableRepository.addCourse(_timetableName.value!!,course)
        }
    }
}

//用于创建ViewModel的工厂方法
class TimetableViewModelFactory(private val app:Application):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TimetableViewModel::class.java)){
            return TimetableViewModel(app) as T
        }
        throw java.lang.IllegalArgumentException("Unkown ViewModel Class")
    }
}