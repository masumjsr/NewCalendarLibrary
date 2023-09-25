package com.example.newcalendarlibrary.room.notes

import android.content.Context
import com.example.newcalendarlibrary.room.AppDatabase
import kotlinx.coroutines.flow.Flow

class  NotesRepository(context: Context) : NoteDAO {
    private val noteDao = AppDatabase.AppDb.getInstance(context).noteDao()
    override suspend fun getNotesByUserId(userId: Int): List<Note> {
        return noteDao.getNotesByUserId(userId)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    override fun getNotes(): List<Note> {
        return noteDao.getNotes()
    }

    override fun deleteNote(note: Note): Int {
        return noteDao.deleteNote(note)
    }
    override fun updateNote(note: Note): Int {
        return noteDao.updateNote(note)
    }

    override fun insertNote(note: Note) {
        return noteDao.insertNote(note)
    }

}