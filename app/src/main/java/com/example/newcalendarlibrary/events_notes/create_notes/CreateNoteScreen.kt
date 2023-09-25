package com.example.newcalendarlibrary.create_notes

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newcalendarlibrary.NoteEvent
import kotlinx.coroutines.launch

/*
@Composable
fun CreateNoteScreen(
    modifier: Modifier = Modifier,
    onEvent: (NoteEvent) -> Unit,
    state : NoteState,
    navController: NavController,
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        AlertDialog(
            onDismissRequest = { onEvent(NoteEvent.HideDialog) },
            // title = { Text(text = "Add Event")},
            shape = RoundedCornerShape(12.dp),
            text = {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    OutlinedTextField(
                        value = state.title,
                        onValueChange = { onEvent(NoteEvent.SetTitle(it)) },
                        // label = { Text(text = "Title") },
                        placeholder = {
                            Text(
                                text = "Title", color = MaterialTheme.colors.onBackground
                            )
                        })
                    Spacer(modifier = modifier.padding(vertical = 5.dp))

//                    OutlinedTextFieldMessage(
//                        modifier.fillMaxWidth().size(150.dp),
//                        state,
//                        onEvent
//                    )
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { onEvent(NoteEvent.SetDescription(it)) },
                        label = { Text("Description") },
                        singleLine = false,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .size(150.dp),
                        shape = RoundedCornerShape(22.dp),
                    )
                    Spacer(modifier = modifier.padding(vertical = 5.dp))

                }
            },
            buttons = {
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(onClick = {
                        coroutineScope.launch {
                           onEvent(NoteEvent.SaveNote)
                            Toast.makeText(context, "Note Saved", Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(context, "Event Updated", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "Save Note")
                    }

                }
            }
        )
    }
}*/


