package com.example.newcalendarlibrary.room.events

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    var title : String,
    val description : String,
    val startTime:Long=0L,
    val endTime:Long=0L,
    val color:Int = 0,
    val user:String=""
)
