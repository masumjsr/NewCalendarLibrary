package com.example.newcalendarlibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.events.EventDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.TITLE)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _appointments = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType.TITLE -> eventDao.getEventOrderedByTitle()
            SortType.DESCRIPTION -> eventDao.getEventOrderedByDescription()
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    }
    private val _state = MutableStateFlow(AppointmentState())

    val state = combine(_state, _sortType, _appointments) { state, sortType, appointment ->
        state.copy(appointment = appointment, sortType = sortType)

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppointmentState())


    fun onEvent(event: AppointmentEvent) {
        when (event) {

            AppointmentEvent.HideDialog -> {
                _state.update { it.copy(isAddingEvent = false) }
            }

            AppointmentEvent.ShowDialog -> {
                _state.update { it.copy(isAddingEvent = true) }
            }

            AppointmentEvent.SaveAppointment -> {
                val title = state.value.title
                val description = state.value.description

                if (title.isBlank() || description.isBlank()) {
                    return
                }

                val eventSaved = Event(title = title, description = description)
                //viewModelScope.launch { eventDao.insertEvent(event = eventSaved) }

                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        isAddingEvent = false
                    )
                }
            }


            is AppointmentEvent.DeleteAppointment -> {
                viewModelScope.launch { eventDao.deleteEvent(event = event.appointment) }
            }

            is AppointmentEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            is AppointmentEvent.SetDescription -> {
                _state.update { it.copy(description = event.description) }
            }

            else -> {}
        }


    }
}


