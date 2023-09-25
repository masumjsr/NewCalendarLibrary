package com.example.newcalendarlibrary.events_notes.create_notes

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.newcalendarlibrary.R
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcalendarlibrary.events_notes.NotesViewModel
import com.example.newcalendarlibrary.room.notes.Note
import com.example.newcalendarlibrary.ui.theme.NewCalendarLibraryTheme
import com.example.newcalendarlibrary.utils.selectedOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditNote(
    noteId: Int,
    navController: NavController,
    viewModel: NotesViewModel
) {
    val scope = rememberCoroutineScope()
    val note = remember { mutableStateOf(Constants.noteDetailPlaceHolder) }

    val currentNote = remember {
        mutableStateOf(note.value.description)
    }

    val currentTitle = remember {
        mutableStateOf(note.value.title)
    }

    val saveButtonState = remember {
        mutableStateOf(false)
    }



    LaunchedEffect(key1 = true) {
        scope.launch(Dispatchers.IO) {
            note.value = viewModel.getNote(noteId) ?: Constants.noteDetailPlaceHolder
            currentNote.value = note.value.description
            currentTitle.value = note.value.title
        }
    }

    NewCalendarLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Scaffold(
                topBar = {
                    AppBar(
                        title = "Edit Note",
                        onIconClick = {
                            viewModel.updateNote(
                                Note(
                                    userId = note.value.userId,
                                    id = note.value.id,
                                    description = currentNote.value,
                                    title = currentTitle.value
                                )
                            )
                            navController.popBackStack()
                            selectedOption  = -1
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = "save note",
                                tint = Color.Black,
                            )
                        },
                        iconState = remember { mutableStateOf(true) }
                    )
                },
            ) {
                Column(
                    Modifier
                        .padding(12.dp)
                        .fillMaxSize()
                ) {

                    TextField(
                        value = currentTitle.value,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black,
                        ), modifier = Modifier.fillMaxWidth(),
                        onValueChange = { value ->
                            currentTitle.value = value
                            if (currentTitle.value != note.value.title) {
                                saveButtonState.value = true
                            } else if (currentNote.value == note.value.description &&
                                currentTitle.value == note.value.title
                            ) {
                                saveButtonState.value = false
                            }
                        },
                        label = { Text(text = "Title", fontWeight = FontWeight.SemiBold) }
                    )

                    Spacer(modifier = Modifier.padding(12.dp))

                    TextField(
                        value = currentNote.value,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black,
                        ),modifier = Modifier.fillMaxWidth(),
                        onValueChange = { value ->
                            currentNote.value = value
                            if (currentNote.value != note.value.description) {
                                saveButtonState.value = true
                            } else if (currentNote.value == note.value.description &&
                                currentTitle.value == note.value.title
                            ) {
                                saveButtonState.value = false
                            }
                        },
                        label = { Text(text = "Note", fontWeight = FontWeight.SemiBold) }
                    )
                }

            }
        }
    }
}