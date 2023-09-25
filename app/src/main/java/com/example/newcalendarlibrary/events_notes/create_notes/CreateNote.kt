package com.example.newcalendarlibrary.events_notes.create_notes

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcalendarlibrary.events_notes.NotesViewModel
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.ui.theme.NewCalendarLibraryTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateNote(
    navController: NavController,
    notesViewModel: NotesViewModel
) {
    val currentNote = remember {
        mutableStateOf("")
    }

    val currentTitle = remember {
        mutableStateOf("")
    }

    val saveButtonState = remember {
        mutableStateOf(false)
    }

    val currentUser = remember {
        mutableStateOf(notesViewModel.userId.value)
    }
    val context = LocalContext.current


    NewCalendarLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Scaffold(
                topBar = {
                    AppBar(
                        title = "Create Note",
                        onIconClick = {

                            notesViewModel.createNote(
                                currentTitle.value,
                                currentNote.value,
                                currentUser.value
                            )
                            println(currentUser)
                            Toast.makeText(context, "Note is saved", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = "save notes",
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(5.dp)),
                        onValueChange = { value ->
                            currentTitle.value = value
                            saveButtonState.value =
                                currentTitle.value != "" && currentNote.value != ""
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Black,
                            focusedLabelColor = Color.Black
                        ),
                        label = { Text(text = "Title") }
                    )
                    Spacer(modifier = Modifier.padding(12.dp))

                    TextField(
                        value = currentNote.value,
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(5.dp)),
                        onValueChange = { value ->
                            currentNote.value = value
                            saveButtonState.value =
                                currentTitle.value != "" && currentNote.value != ""
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Black,
                            focusedLabelColor = Color.Black
                        ),
                        label = { Text(text = "Note") }
                    )
                }

            }
        }
    }
}