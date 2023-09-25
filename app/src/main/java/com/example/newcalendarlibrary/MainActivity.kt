package com.example.newcalendarlibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.newcalendarlibrary.events_notes.NotesViewModel
import com.example.newcalendarlibrary.events_notes.NotesViewModelFactory
import com.example.newcalendarlibrary.navigation.HomeNavGraph
import com.example.newcalendarlibrary.navigation.components.BottomNav
import com.example.newcalendarlibrary.room.AppDatabase
import com.example.newcalendarlibrary.room.events.EventDatabase
import com.example.newcalendarlibrary.room.notes.NotesRepository
import com.example.newcalendarlibrary.room.user.UserRepository
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var notesViewModel: NotesViewModel


    private val db by lazy {
        Room.databaseBuilder(applicationContext, EventDatabase::class.java, "appointment.db")
            .build()
    }
    private val viewModel by viewModels<EventViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EventViewModel(db.eventDao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notesRepository = NotesRepository(context = applicationContext)
        val userRepository = UserRepository(context = applicationContext)

        AppDatabase.AppDb.getInstance(this).noteDao()
        val notesViewModelFactory = NotesViewModelFactory(notesRepository, userRepository)

        notesViewModel = ViewModelProvider(this, notesViewModelFactory)[NotesViewModel::class.java]


        setContent {

            val state by viewModel.state.collectAsState()
            BottomNav(
                onEvent = viewModel::onEvent,
                eventDao = db.eventDao,
                state = state,
                notesViewModel = notesViewModel,
                navController =  rememberNavController()
            )
            //LoginScreen(notesViewModel = notesViewModel)

        }
    }

}


