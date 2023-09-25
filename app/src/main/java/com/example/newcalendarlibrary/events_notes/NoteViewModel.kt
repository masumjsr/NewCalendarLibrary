package com.example.newcalendarlibrary.events_notes

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newcalendarlibrary.NoteEvent
import com.example.newcalendarlibrary.SortType
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.create_notes.NoteState
import com.example.newcalendarlibrary.room.AppDatabase
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.notes.Note
import com.example.newcalendarlibrary.room.notes.NotesRepository
import com.example.newcalendarlibrary.room.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repo: NotesRepository, private val userRepo: UserRepository
) : ViewModel() {

    var userId = mutableStateOf(0)
    val notes = mutableStateOf<List<Note>>(emptyList())
    private var username = ""

    init {
        viewModelScope.launch {
            notes.value = repo.getNotesByUserId(userId.value)
            println("init $userId")
        }
    }

    fun getUserName(userId: Int): String {
        viewModelScope.launch {
            username = userRepo.getUserName(userId)
        }
        return username
    }

    private suspend fun getNotesByUserId(userId: Int): List<Note> {
        return repo.getNotesByUserId(userId)
    }

    fun reloadNotes(userId: Int) {
        viewModelScope.launch {
            notes.value = repo.getNotesByUserId(userId)
        }
    }

    fun deleteNotes(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteNote(note)
            notes.value = getNotesByUserId(userId.value)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateNote(note)
            notes.value = getNotesByUserId(userId.value)
        }
    }

    fun createNote(title: String, note: String, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = Note(title = title, description = note, userId = userId)
            repo.insertNote(newNote)
            notes.value = getNotesByUserId(userId = userId)
        }
    }

    suspend fun getNote(noteId: Int): Note? {
        return repo.getNoteById(noteId)
    }

}

/**
 * @NotesViewModelFactory **
 * We need to create a Factory when our ViewModel have parameters in the constructor and
 * in order to create the instance of the ViewModel, we need to create its factory just like below
 * with required parameters. Hope it helps :).
 * */
@Suppress("UNCHECKED_CAST")
class NotesViewModelFactory(
    private val repo: NotesRepository, private val userRepo: UserRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotesViewModel(
            repo = repo, userRepo = userRepo,
        ) as T
    }

}


