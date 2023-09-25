package com.example.newcalendarlibrary.utils

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ContentAlpha
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.calendar.CalendarScreen
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.events_notes.NotesViewModel
import com.example.newcalendarlibrary.events_notes.create_notes.NoteList
import com.example.newcalendarlibrary.settings.SettingsScreen
var selectedOption by  mutableIntStateOf(1)
@Composable
fun RadioButtonsNavigation(
    navController: NavHostController,
    notesViewModel: NotesViewModel,
    userId: Int,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    navigateToItemUpdate: (Int) -> Unit

) {

    var selectedColor by remember { mutableStateOf(colors[0]) }

    Column {
        Row(
            Modifier
                .wrapContentSize()
                .padding(start = 4.dp, end = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedOption == -1,
                onClick = { selectedOption = -1 },
                modifier = Modifier.weight(1f)
            )
            Text(text = "Notes")

            RadioButton(
                selected = selectedOption == 1,
                onClick = { selectedOption = 1 },
                modifier = Modifier.weight(1f)
            )
            Text(text = "Calendar")




            RadioButton(
                selected = selectedOption == 2,
                onClick = { selectedOption = 2 },
                modifier = Modifier.weight(1f)
            )
            Text(text = "Settings")


        }

        when (selectedOption) {

            0 -> {
                NoteList(
                    navController = navController,
                    notesViewModel = notesViewModel,
                    userId = userId,
                    state = state,
                    onEvent = onEvent,
                    navigateToItemUpdate = navigateToItemUpdate
                )
            }

            1 -> {
                CalendarScreen(
                    state = state,
                    onEvent = onEvent,
                    navigateToItemUpdate = navigateToItemUpdate
                )
            }

            2 -> {
                SettingsScreen(
                    navController = navController,
                    modifier = Modifier,
                    colors = colors,
                    onColorSelected = { selectedColor = it }
                )
            }

        }
    }
}