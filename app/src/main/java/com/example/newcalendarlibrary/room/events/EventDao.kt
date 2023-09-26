package com.example.newcalendarlibrary.room.events

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
@Dao
interface EventDao {

    @Upsert
    suspend fun insertEvent(event: Event)
    @Update
    suspend fun updateEvent(event: Event)
    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("SELECT * FROM event ORDER BY title ASC")
    fun getEventOrderedByTitle(): Flow<List<Event>>

    @Query("SELECT * FROM event ORDER BY description ASC")
    fun getEventOrderedByDescription(): Flow<List<Event>>




  @Query("SELECT * FROM event where user=:user")
    fun getEvent(user: String): Flow<List<Event>>


 @Query("SELECT * FROM event where id=:id")
    fun getEvent(id:Int): Flow<Event>


    @Query("SELECT * FROM event where startTime between :startTime and:endTime and user=:user")
    fun getEvent(startTime: Long, endTime: Long, user: String):Flow<List<Event>>

}