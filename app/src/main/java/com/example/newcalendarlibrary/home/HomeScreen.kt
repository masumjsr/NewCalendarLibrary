package com.example.newcalendarlibrary.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newcalendarlibrary.NoteEvent
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.color_picker.colourSaver
import com.example.newcalendarlibrary.create_notes.NoteState
import com.example.newcalendarlibrary.room.notes.Note
import com.example.newcalendarlibrary.ui.viewmodel.AddEventViewModel


private const val TAG = "HomeScreen"
@SuppressLint("QueryPermissionsNeeded")
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    colors: List<Color>,
    selectedColor: Color,
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
    userId : Int // load note based on user login info
) {
    var currentlySelected by rememberSaveable(saver = colourSaver()) { mutableStateOf(colors[0]) }
    val context = LocalContext.current
    // Testing login user
    Toast.makeText(context, "Welcome $userId", Toast.LENGTH_SHORT).show()

    if (state.isAddingNote) {
       // CreateNoteScreen(onEvent = onEvent, state = state, navController = navController)
        Toast.makeText(context, "Add notes", Toast.LENGTH_SHORT).show()
    }

    // Define the URL you want to open
    val url = "https://www.google.com"

    // Handle the FOB click event
    val onFobClick = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // Specify the package name for Google Chrome (com.android.chrome)
        intent.setPackage("com.android.chrome")

        // Check if there's an activity to handle the intent
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // If Google Chrome is not installed, open the URL in the default browser
            val defaultBrowserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            defaultBrowserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(defaultBrowserIntent)
        }
    }


    //UI
    Surface(modifier = modifier.fillMaxSize(), color = selectedColor) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var tabIndex by remember { mutableStateOf(0) }

            val tabs = listOf("Today", "This Week")

            ElevatedCard {
                TabRow(selectedTabIndex = tabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index }
                        )
                    }
                }
            }


            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.clickable {
                    onEvent(NoteEvent.ShowDialog)
                   Log.d(TAG,"Note btn clicked")
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_library_add_24),
                    contentDescription = "add notes button",
                )
                Card(modifier.wrapContentSize(), elevation = 6.dp) {
                    Text(text = "Create Notes", textAlign = TextAlign.Center)
                }
            }

            NotesList(onEvent = onEvent, itemList = state.notes, onItemClick = {})

        }
        Box(contentAlignment = Alignment.BottomEnd) {
            FloatingActionButton(
                onClick = onFobClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_travel_explore_24),
                    contentDescription = null
                )
            }
        }
    }

}


@Composable
fun NotesList(
    onEvent: (NoteEvent) -> Unit,
    itemList: List<Note>, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        if (itemList.isEmpty()) {
            Text(
                text = "Please click on the date to add notes",
                textAlign = TextAlign.Center,
            )
        } else {
            InventoryList(
                itemList = itemList,
                onItemClick = { onItemClick(it.id) },
                onEvent = onEvent
            )
        }
    }

}

@Composable
private fun InventoryList(
    onEvent: (NoteEvent) -> Unit,
    itemList: List<Note>, onItemClick: (Note) -> Unit, modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items(items = itemList, key = { it.id }) { item ->
            NoteCard(
                note = item,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onItemClick(item)
                    },
                onEvent = onEvent
            )
        }
    }
}


@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    onEvent: (NoteEvent) -> Unit,
) {

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 16.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Column(
                modifier = modifier
                    .wrapContentSize()
                    .weight(1f)
            ) {
                Text(text = note.title, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = note.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                )
            }

            IconButton(onClick = {
                onEvent(NoteEvent.DeleteNote(note))
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete contact"
                )
            }


        }
    }

}