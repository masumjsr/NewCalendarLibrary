package com.example.newcalendarlibrary.create_notes

import com.example.newcalendarlibrary.SortType
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.notes.Note

data class AppointmentState(
    val appointment : List<Event> = emptyList(),
    val title : String = "",
    val description : String = "",
    val isAddingEvent : Boolean = false,
    val sortType: SortType = SortType.TITLE

)

data class NoteState(
    val notes : List<Note> = emptyList(),
    val title : String = "",
    val description : String = "",
    val isAddingNote : Boolean = false,
    val sortType: SortType = SortType.TITLE,
    val noteId : Long = 0,
    val userId : Long = 0

)