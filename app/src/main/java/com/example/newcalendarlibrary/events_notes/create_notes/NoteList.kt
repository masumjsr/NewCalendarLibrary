package com.example.newcalendarlibrary.events_notes.create_notes


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.events_notes.NotesViewModel
import com.example.newcalendarlibrary.register.UserViewModel
import com.example.newcalendarlibrary.room.notes.Note
import com.example.newcalendarlibrary.ui.theme.NewCalendarLibraryTheme
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.calendar.EventCard
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.home.NoteCard
import com.example.newcalendarlibrary.navigation.Screens
import com.example.newcalendarlibrary.ui.viewmodel.AddEventViewModel
import com.example.newcalendarlibrary.ui.viewmodel.SettingViewModel
import com.example.newcalendarlibrary.utils.RadioButtonsNavigation


@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter",
    "StateFlowValueCalledInComposition", "QueryPermissionsNeeded"
)
@Composable
fun NoteList(
    navController: NavHostController,
    notesViewModel: NotesViewModel,
    userId: Int,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: AddEventViewModel = hiltViewModel(),
    settingViewModel: SettingViewModel = hiltViewModel(),
) {

    val userViewModel: UserViewModel = viewModel()
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val dialogOpen = remember { mutableStateOf(false) }
    val deleteText = remember {
        mutableStateOf("")
    }


    val noteQuery = remember {
        mutableStateOf("")
    }

    val notesDelete = remember {
        mutableStateOf(listOf<Note>())
    }

    notesViewModel.userId = remember {
        mutableStateOf(userId)
    }

    val openDialog = remember { mutableStateOf(false) }

    val notes = notesViewModel.notes

    val username = notesViewModel.getUserName(userId)

    val context = LocalContext.current

    val sortDateChange = remember { mutableStateOf(true) }
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

    DisposableEffect(key1 = backPressedDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialogOpen.value = true
            }
        }

        backPressedDispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }

    if (dialogOpen.value) {
        LogoutDialog(
            onConfirmLogout = {
                userViewModel.logout()
                navController.navigate(Screens.LoginScreen.route)
                dialogOpen.value = false
            },
            onDismiss = {
                dialogOpen.value = false
            }
        )
    }
    var isPlaying by remember {
        mutableStateOf(true)
    }

// for speed
    var speed by remember {
        mutableStateOf(1f)
    }


    NewCalendarLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize().background(colors[settingViewModel.setting]), color = colors[settingViewModel.setting]) {
            Scaffold(
                topBar = {
                    AppBar(
                        title = "Welcome $username!",
                        onIconClick = {
                            // if (notes.value.isNotEmpty()) {
                            dialogOpen.value = true
                            // deleteText.value = "Are you sure you want to delete all notes ?"
                            // notesDelete.value = notes.value
                            /*} else {
                                Toast.makeText(context, "No Notes found.", Toast.LENGTH_SHORT)
                                    .show()
                            }*/
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.logout),
                                contentDescription = "logout",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        iconState = remember { mutableStateOf(true) }

                    )
                },
                floatingActionButton = {
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

            ) {
                Column (
                    modifier= Modifier.fillMaxSize().background(colors[settingViewModel.setting]),
                ){

                    val today by viewModel.todayEvent.collectAsStateWithLifecycle()
                    val thisWeek by viewModel.weekEvent.collectAsStateWithLifecycle()

                    RadioButtonsNavigation(
                        navController = navController,
                        notesViewModel = notesViewModel,
                        userId = userId,
                        state = state,
                        onEvent = onEvent,
                        navigateToItemUpdate = navigateToItemUpdate
                    )


                        var tabIndex by remember { mutableStateOf(0) }

                        val tabs = listOf("Today", "This Week")

                        ElevatedCard(
                            modifier= Modifier.height(300.dp),
                        ) {
                            androidx.compose.material3.TabRow(selectedTabIndex = tabIndex) {
                                tabs.forEachIndexed { index, title ->
                                    androidx.compose.material3.Tab(text = { Text(title) },
                                        selected = tabIndex == index,
                                        onClick = { tabIndex = index }
                                    )
                                }
                            }

                            LazyColumn( verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(items = if(tabIndex==0)today else thisWeek, key = { it.id }) { item ->
                                    EventCard(
                                        appointment = item,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {

                                            },
                                        onEvent = onEvent,
                                        onDeleteEvent = viewModel::deleteEvent
                                    )
                                }
                            }


                        }





                    SearchBar(noteQuery)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.sort),
                            contentDescription = "sort",
                            modifier = Modifier
                                .padding(12.dp)
                                .clickable {
                                    sortDateChange.value = sortDateChange.value.not()
                                    notesViewModel.reloadNotes(userId)
                                }
                        )
                    }
                    NotesList(
                        notes = notes.value,
                        query = noteQuery,
                        openDialog = openDialog,
                        deleteText = deleteText,
                        navController = navController,
                        notesToDelete = notesDelete,
                        sortDateChange = sortDateChange
                    )
                }

                DeleteDialog(
                    openDialog = openDialog,
                    text = deleteText,
                    notesToDelete = notesDelete,
                    action = {
                        notesDelete.value.forEach {
                            notesViewModel.deleteNotes(it)
                        }
                    })
            }

        }
    }
}

@Composable
fun SearchBar(query: MutableState<String>) {
    Column(Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)) {
        TextField(
            value = query.value,
            placeholder = { Text("Search note...") },
            maxLines = 1,
            onValueChange = { query.value = it },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            ),
            trailingIcon = {
                AnimatedVisibility(
                    visible = query.value.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = { query.value = "" }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.plus),
                            contentDescription = "search"
                        )
                    }
                }

            })

    }
}

@Composable
fun NotesList(
    notes: List<Note>,
    openDialog: MutableState<Boolean>,
    query: MutableState<String>,
    deleteText: MutableState<String>,
    navController: NavController,
    notesToDelete: MutableState<List<Note>>,
    sortDateChange: MutableState<Boolean>
) {
    val queriedNotes = if (query.value.isEmpty()) {
        if (sortDateChange.value) {
            notes.sortedByDescending { it.dateUpdated }
        } else {
            notes.sortedBy { it.dateUpdated }
        }
    } else {
        notes.filter { it.description.contains(query.value) || it.title.contains(query.value) }
    }

    if (queriedNotes.isEmpty()) {

        Text(
            text = "No notes found. Please add new note.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp, fontWeight = FontWeight.SemiBold
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(start = 20.dp)
                .clickable {
                    navController.navigate("createnote_page")
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_library_add_24),
                contentDescription = "add notes button",
            )
            Card(Modifier.wrapContentSize(), elevation = 6.dp) {
                Text(text = "Create Notes", textAlign = TextAlign.Center)
            }
        }


    } else {
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
        ) {
            // var previousHeader = ""
            itemsIndexed(queriedNotes) { _, note ->
                /*   if (note.getDay() != previousHeader) {
                       Column(
                           modifier = Modifier
                               .padding(10.dp)
                               .fillMaxWidth()
                       ) {
                           Text(
                               text = note.getDay(),
                               color = Color.Black,
                               fontWeight = FontWeight.SemiBold,
                               fontSize = 18.sp
                           )
                       }
                       Spacer(
                           modifier = Modifier
                               .fillMaxWidth()
                               .height(10.dp)
                       )
                       previousHeader = note.getDay()
                   }*/

                NoteListItem(
                    note,
                    openDialog,
                    deleteText = deleteText,
                    navController,
                    notesToDelete = notesToDelete,
                    noteBackGround = Color.Black
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                )
            }
            item {
                NotesFloatingActionButton(
                    contentDescription = "create notes",
                    action = { navController.navigate("createnote_page") },
                    icon = R.drawable.note_add_icon
                )
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(
    note: Note,
    openDialog: MutableState<Boolean>,
    deleteText: MutableState<String>,
    navController: NavController,
    noteBackGround: Color,
    notesToDelete: MutableState<List<Note>>
) {

    return Box(
        modifier = Modifier
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .background(noteBackGround)
                .fillMaxWidth()
                .height(120.dp)
                .combinedClickable(interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = {
                        if (note.id != 0) {
                            navController.navigate(Constants.noteDetailNavigation(note.id))
                        }
                    },
                    onLongClick = {
                        if (note.id != 0) {
                            openDialog.value = true
                            println(note.id)
                            deleteText.value = "Are you sure you want to delete this note ?"
                            notesToDelete.value = mutableListOf(note)
                        }
                    }
                )

        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = note.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
                Text(
                    text = note.description,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomEnd)
            ) {
                Text(
                    text = note.dateUpdated,
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                )
            }
        }
    }
}

@Composable
fun NotesFloatingActionButton(contentDescription: String, icon: Int, action: () -> Unit) {
    return FloatingActionButton(
        onClick = { action.invoke() },
        backgroundColor = Color.Black,
        contentColor = Color.White,
        modifier = Modifier
            .border(2.dp, Color.White, CircleShape)
    ) {
        Icon(
            ImageVector.vectorResource(id = icon),
            contentDescription = contentDescription,
            tint = Color.White
        )

    }
}

@Composable
fun DeleteDialog(
    openDialog: MutableState<Boolean>,
    text: MutableState<String>,
    action: () -> Unit,
    notesToDelete: MutableState<List<Note>>
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Delete Note")
            },
            text = {
                Column {
                    Text(text.value)
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = {
                                action.invoke()
                                openDialog.value = false
                                notesToDelete.value = mutableListOf()
                            }
                        ) {
                            Text("Yes")
                        }
                        Spacer(modifier = Modifier.padding(12.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = {
                                openDialog.value = false
                                notesToDelete.value = mutableListOf()
                            }
                        ) {
                            Text("No")
                        }
                    }

                }
            }
        )
    }
}

@Composable
fun LogoutDialog(
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Logout", fontWeight = FontWeight.Bold)
        },
        text = {
            Text("Are you sure you want to logout?", fontWeight = FontWeight.SemiBold)
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                onClick = onConfirmLogout
            ) {
                Text("Logout", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                onClick = onDismiss
            ) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}