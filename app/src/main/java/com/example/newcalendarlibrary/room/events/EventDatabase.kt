package com.example.newcalendarlibrary.room.events

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {

    abstract val eventDao : EventDao
}