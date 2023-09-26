package com.example.newcalendarlibrary.repository

import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.events.EventDao
import com.example.newcalendarlibrary.utils.MyPreference
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject


fun endDate(currentTimeMillis: Long=System.currentTimeMillis()): Date {
    val c: Calendar = GregorianCalendar()
    c.timeInMillis=currentTimeMillis
    c.set(Calendar.HOUR_OF_DAY, 23) //anything 0 - 23

    c.set(Calendar.MINUTE, 59)
    c.set(Calendar.SECOND, 59)
    val endDate: Date = c.time
    return endDate
}

fun startDate(currentTimeMillis: Long=System.currentTimeMillis()): Date {
    val startTime: Calendar = GregorianCalendar()
    startTime.timeInMillis=currentTimeMillis
    startTime.set(Calendar.HOUR_OF_DAY, 0) //anything 0 - 23

    startTime.set(Calendar.MINUTE, 0)
    startTime.set(Calendar.SECOND, 0)
    val startDate: Date = startTime.time
    return startDate
}

class EventRepository @Inject constructor(private val eventDao: EventDao,val preference: MyPreference) {

    fun todayEvent (): Flow<List<Event>> {
        val startDate: Date = startDate(System.currentTimeMillis())

        val endDate: Date = endDate(System.currentTimeMillis())

        return eventDao.getEvent(startDate.time,endDate.time,user=preference.getUser())

    }


    fun getEvent()=eventDao.getEvent(user=preference.getUser())
    fun getEvent(id:Int)=eventDao.getEvent(id)



    fun thisWeekEvent (): Flow<List<Event>> {
        val startTime: Calendar = GregorianCalendar()
        startTime.set(Calendar.DAY_OF_WEEK, startTime.firstDayOfWeek)
        startTime.set(Calendar.HOUR_OF_DAY, 0) //anything 0 - 23

        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.SECOND, 0)
        val startDate: Date = startTime.time

        val c: Calendar = GregorianCalendar()
        c.set(Calendar.DAY_OF_WEEK, 6)

        c.set(Calendar.HOUR_OF_DAY, 23) //anything 0 - 23

        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND,59)
        val endDate: Date = c.time

        return eventDao.getEvent(startDate.time, endDate.time, preference.getUser())

    }

    suspend  fun storeEvent(event: Event) {
    eventDao.insertEvent(event)
    }
    suspend fun removeEvent(event: Event) =eventDao.deleteEvent(event)
}