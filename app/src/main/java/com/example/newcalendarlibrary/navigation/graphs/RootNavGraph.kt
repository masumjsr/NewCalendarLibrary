package com.example.newcalendarlibrary.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.NoteEvent
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.create_notes.NoteState
import com.example.newcalendarlibrary.navigation.Graph
import com.example.newcalendarlibrary.navigation.components.BottomNav
import com.example.newcalendarlibrary.room.events.EventDao

/*@Composable
fun RootNavGraph(
    navController: NavHostController,
    onEvent: (AppointmentEvent) -> Unit,
    eventDao: EventDao, state: AppointmentState,
    stateNote: NoteState,
    onEventNote: (NoteEvent) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = Graph.AUTHENTICATION,
        route = Graph.ROOT
    ) {
       composable(route = Graph.HOME){
           BottomNav(onEvent, eventDao, state,navController)
       }


    }
}*/
/*
class NoteViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class EventViewModelFactory(private val eventDao: EventDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppointmentViewModel(eventDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
@Composable
fun RootNavGraph(
    navController: NavHostController,
    onEvent: (AppointmentEvent) -> Unit,
    eventDao: EventDao,
    state: AppointmentState,
    stateNote: NoteState,
    onEventNote: (NoteEvent) -> Unit,
    userRepository: UserRepository,
) {
    var selectedColor by remember { mutableStateOf(colors[0]) }
    val applicationContext = LocalContext.current
    val db by lazy {
        Room.databaseBuilder(applicationContext, EventDatabase::class.java, "appointment.db")
            .build()
    }

    val dbNote by lazy {
        Room.databaseBuilder(applicationContext, NoteDatabase::class.java, "notes.db")
            .build()
    }



    val noteViewModel: NoteViewModel = viewModel(
        factory = NoteViewModelFactory(dbNote.noteDao)
    )

    val eventViewModel: AppointmentViewModel = viewModel(
        factory = EventViewModelFactory(db.eventDao)
    )



    NavHost(
        navController = navController,
        startDestination = Graph.AUTHENTICATION,
        route = Graph.ROOT
    ) {
        // Authentication NavHost
        navigation(route = Graph.AUTHENTICATION, startDestination = Screens.LoginScreen.route) {
            composable(route = Screens.LoginScreen.route) {
                LoginScreen(navController = navController, userRepository = userRepository)
            }
            composable(route = Screens.SignUpScreen.route) {
                SignUpScreen(navController = navController, userRepository = userRepository)
            }
            // Add more authentication destinations as needed
        }

        // Home NavHost
        navigation(route = Graph.HOME, startDestination = BottomBarScreen.Home.route) {

            composable(route = BottomBarScreen.Home.route) {
                HomeScreen(
                    navController = navController,
                    colors = colors,
                    selectedColor = selectedColor,
                    state = stateNote,
                    onEvent = onEventNote
                )
            }
            composable(route = BottomBarScreen.Profile.route) {
                CalendarScreen(
                    state = state,
                    onEvent = onEvent,
                    navigateToItemUpdate = {
                        navController.navigate("${Screens.EventUpdateScreen.route}/${it}")
                    }
                )
            }
            composable(route = Screens.AddEventScreen.route) {
                AddEventScreen(state = state, onEvent = onEvent)
            }
            composable(
                route = "${Screens.EventUpdateScreen.route}/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                    }
                )
            ) { navBackStackEntry ->
                val itemId = navBackStackEntry.arguments!!.getInt("id")
                EventUpdateScreen(
                    itemId = itemId,
                    onEvent = onEvent,
                    state = state,
                    navController = navController,
                    eventDao = eventDao
                )
            }
            composable(route = BottomBarScreen.Settings.route) {
                SettingsScreen(
                    navController = navController,
                    colors = colors,
                    onColorSelected = { selectedColor = it }
                )
            }
        }
    }
}*/
