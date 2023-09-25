package com.example.newcalendarlibrary

import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.notes.Note

sealed interface AppointmentEvent {
    object SaveAppointment : AppointmentEvent
    object HideDialog : AppointmentEvent
    object ShowDialog : AppointmentEvent
    data class SetTitle(val title: String) : AppointmentEvent
    data class SetDescription(val description: String) : AppointmentEvent
    data class DeleteAppointment(val appointment: Event) : AppointmentEvent

}

sealed interface NoteEvent {
    object SaveNote : NoteEvent
    object HideDialog : NoteEvent
    object ShowDialog : NoteEvent
    data class SetTitle(val title: String) : NoteEvent
    data class SetDescription(val description: String) : NoteEvent
    data class DeleteNote(val note: Note) : NoteEvent

}