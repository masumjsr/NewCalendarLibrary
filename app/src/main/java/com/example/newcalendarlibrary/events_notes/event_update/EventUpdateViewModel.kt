package com.example.newcalendarlibrary.event_update

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.events.EventDao

class EventUpdateViewModel(private val eventDao: EventDao, itemId: Int) :  ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")

    // Function to update the event
    suspend fun updateEventInDatabase(itemId: Int) {
        val eventToUpdate = Event(id = itemId, title = title, description = description)
        eventDao.updateEvent(eventToUpdate)
    }
}

class EventUpdateViewModelFactory(
    private val eventDao: EventDao,
    private val itemId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(EventUpdateViewModel::class.java)) {
            return EventUpdateViewModel(eventDao, itemId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}