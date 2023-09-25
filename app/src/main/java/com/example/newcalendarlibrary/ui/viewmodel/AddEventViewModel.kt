package com.example.newcalendarlibrary.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcalendarlibrary.repository.EventRepository
import com.example.newcalendarlibrary.repository.endDate
import com.example.newcalendarlibrary.repository.startDate
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.utils.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class  AddEventViewModel @Inject constructor (
    val eventRepository: EventRepository,
    val preference: MyPreference
):ViewModel(){
    val user=preference.getUser()
    fun storeEvent(event: Event){
        viewModelScope.launch {
        eventRepository.storeEvent(event)
        }
    }

    val staring = MutableStateFlow(startDate().time)
    val ending = MutableStateFlow(endDate().time)
    var color = MutableStateFlow(0)
    
    init {

        Log.i("123321", "${color}: ")
    }

    fun updateColor(colorIndex: Int){
       color.value=colorIndex
    }

    fun setValue(start:Long,end:Long){
        staring.value=start
        ending.value=end
    }
    val todayEvent=eventRepository.todayEvent()
        .stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = emptyList()
    )

    val data=eventRepository.getEvent()
    val rangeEvent=data
        .combine(staring){a, b ->
            return@combine a.filter { it.startTime >b }
        }
        .combine(ending){
            a,b->
            return@combine a.filter { it.startTime<b}
        }
        .stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = emptyList()
    )

    var colorEvent=eventRepository.getEvent()
        .combine(color){a,b ->
            return@combine a.filter { it.color==b }
        }
        .stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = emptyList()
    )

    fun  deleteEvent(event: Event){
        colorEvent
        viewModelScope.launch {
            eventRepository.removeEvent(event)
        }
    }

    val weekEvent=eventRepository.thisWeekEvent()
        .stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = emptyList()
    )
}