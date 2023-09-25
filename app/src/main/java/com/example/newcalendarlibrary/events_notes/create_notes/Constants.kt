package com.example.newcalendarlibrary.events_notes.create_notes

import com.example.newcalendarlibrary.room.notes.Note

object Constants {
    const val NOTES_TABLE_NAME = "notes"
    const val USERS_TABLE_NAME = "users"
    const val DATABASE_NAME = "appDatabase"

    fun noteDetailNavigation(noteId: Int) = "noteDetail/$noteId"
    fun noteEditNavigation(noteId: Int) = "editNote/$noteId"

    val noteDetailPlaceHolder =
        Note(description = "Cannot find note details", id = 0, title = "Cannot find note details")
}