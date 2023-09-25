package com.example.newcalendarlibrary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.event_update.EventUpdateScreen
import com.example.newcalendarlibrary.events_notes.NotesViewModel
import com.example.newcalendarlibrary.events_notes.create_notes.CreateNote
import com.example.newcalendarlibrary.events_notes.create_notes.EditNote
import com.example.newcalendarlibrary.events_notes.create_notes.NoteDetails
import com.example.newcalendarlibrary.events_notes.create_notes.NoteList
import com.example.newcalendarlibrary.navigation.components.BottomBarScreen
import com.example.newcalendarlibrary.register.LoginPage
import com.example.newcalendarlibrary.register.RegisterPage
import com.example.newcalendarlibrary.room.events.EventDao

/**
 * This NavGraph is the real deal "🤯"
 * */
@Composable
fun HomeNavGraph(
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    eventDao: EventDao,
    notesViewModel: NotesViewModel,
    navController: NavHostController,
    navigateToItemUpdate: (Int) -> Unit
) {

    var selectedColor by remember { mutableStateOf(colors[0]) }

    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.route
    ) {
        composable(
            route = Screens.LoginScreen.route
        ) {
            LoginPage(navController = navController, notesViewModel = notesViewModel)
        }
        composable(
            route = Screens.SignUpScreen.route
        ) {
            RegisterPage(navController = navController)
        }

        composable(
            route = "${BottomBarScreen.Home.route}/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) {
            val userId = remember {
                it.arguments?.getInt("userId")
            }
            if (userId != null) {
                NoteList(
                    navController,
                    notesViewModel,
                    userId,
                    state,
                    onEvent,
                    navigateToItemUpdate
                )
            }
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

        composable("createnote_page") {
            CreateNote(navController = navController, notesViewModel)
        }

        composable(
            "noteDetail/{noteId}",
            arguments = listOf(navArgument("noteId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("noteId")
                ?.let { NoteDetails(noteId = it, navController, notesViewModel) }
        }

        composable(
            "editNote/{noteId}",
            arguments = listOf(navArgument("noteId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("noteId")
                ?.let { EditNote(noteId = it, navController, notesViewModel) }
        }
    }
}


