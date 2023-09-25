package com.example.newcalendarlibrary.calendar

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newcalendarlibrary.events_notes.AddEventScreen
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.room.events.Event
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.color_picker.ColourButton
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.displayText
import com.example.newcalendarlibrary.repository.endDate
import com.example.newcalendarlibrary.repository.startDate
import com.example.newcalendarlibrary.ui.viewmodel.AddEventViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.YearMonth
import java.util.Date
import java.util.Locale
import java.util.logging.SimpleFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: AddEventViewModel = hiltViewModel(),
) {
    val todayEvent by viewModel.rangeEvent.collectAsStateWithLifecycle()
    val colorEvent by viewModel.colorEvent.collectAsStateWithLifecycle()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val events1 = (0..1).map {
            KalendarEvent(
                date = Clock.System.todayIn(
                    TimeZone.currentSystemDefault()
                ).plus(it, DateTimeUnit.DAY),
                eventName = it.toString(),
                eventDescription = it.toString()
            )
        }


        var showTimePicker by remember {
            mutableStateOf(false)
        }

        val rangeState= rememberDateRangePickerState(
            initialSelectedStartDateMillis = System.currentTimeMillis(),
            initialSelectedEndDateMillis = System.currentTimeMillis()
        )
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            Button(onClick = {

                onEvent(AppointmentEvent.ShowDialog)
            }) {
                Text(text = "Add Event")
                
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(4.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 8.dp
            ) {

                var tabIndex by remember { mutableStateOf(0) }

                val currentMonth = remember { YearMonth.now() }
                val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
                val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
                val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
                val tabs = listOf("By Time", "By Color")
                val state = rememberCalendarState(
                    startMonth = startMonth,
                    endMonth = endMonth,
                    firstVisibleMonth = currentMonth,
                    firstDayOfWeek = firstDayOfWeek
                )
                val daysOfWeek = remember { daysOfWeek() }


                ElevatedCard {
                    androidx.compose.material3.TabRow(selectedTabIndex = tabIndex) {
                        tabs.forEachIndexed { index, title ->
                            androidx.compose.material3.Tab(text = { Text(title) },
                                selected = tabIndex == index,
                                onClick = { tabIndex = index }
                            )
                        }
                    }

                  when(tabIndex){
                      0->{


                          var starting by remember {
                              mutableStateOf(startDate())
                          }
                          var ending by remember {
                              mutableStateOf(endDate())
                          }

                          var text by remember {
                              mutableStateOf("${SimpleDateFormat("dd MMM yyyy").format(starting)}- ${SimpleDateFormat("dd MMM yyyy").format(ending)}")
                          }

                         CustomTimePicker(showDatePicker =showTimePicker , datePickerState =rangeState ) {
                            showTimePicker=false
                             Log.i("123321", "CalendarScreen: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(rangeState.selectedStartDateMillis?:0))}")

                             if(rangeState.selectedStartDateMillis==null){
                                 starting= startDate()
                                 ending= endDate()
                             }
                             else if(rangeState.selectedEndDateMillis==null){
                                 starting= startDate(rangeState.selectedStartDateMillis!!)
                                 ending= endDate(rangeState.selectedStartDateMillis!!)

                             }
                              else{
                                 starting= startDate(rangeState.selectedStartDateMillis!!)
                                 ending= endDate(rangeState.selectedEndDateMillis!!)
                             }
                             text="${SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(starting)}- ${SimpleDateFormat("dd MMM yyyy",
                                 Locale.ENGLISH).format(ending)}"
                             viewModel.setValue(starting.time,ending.time)

                         }



                          Row(

                              modifier= Modifier
                                  .padding(4.dp)
                                  .fillMaxWidth(),
                              horizontalArrangement = Arrangement.SpaceBetween,
                              verticalAlignment = Alignment.CenterVertically
                          ){
                              Text(text = text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                              IconButton(onClick = { showTimePicker=true}) {
                                  Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                              }
                          }

                          HomeBody(
                              onEvent = onEvent,
                              itemList = todayEvent,
                              onItemClick = navigateToItemUpdate,
                              modifier = modifier.padding(top = 20.dp),
                              onDeleteEvent = viewModel::deleteEvent
                          )
                      }
                      1->{

                          Log.i("123321", "CalendarScreen: $colorEvent")
                          ColourButton(
                              colors = colors,
                              onColorSelected = {
                                  viewModel.updateColor(it)
                              },
                              modifier = modifier
                                  .fillMaxWidth()
                                  .padding(start = 16.dp, end = 16.dp),
                          )

                          HomeBody(
                              onEvent = onEvent,
                              itemList = colorEvent,
                              onItemClick = navigateToItemUpdate,
                              modifier = modifier.padding(top = 20.dp),
                              onDeleteEvent = viewModel::deleteEvent
                          )
                      }
                  }


                }
            }
            if (state.isAddingEvent) {
                AddEventScreen(state = state, onEvent = onEvent)

            }


        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CustomTimePicker(
    showDatePicker: Boolean,
    datePickerState: DateRangePickerState,
    onDismiss:()->Unit
) {
    var showDatePicker1 = showDatePicker
    if (showDatePicker1) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface),
            onDismissRequest = { onDismiss.invoke() },
            properties = DialogProperties(usePlatformDefaultWidth = false) // set this so that the dialog occupies the full width and height
        ) {
            Column(
                modifier = Modifier

                    .padding(
                        top = 16.dp,
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 0.dp
                    ), // reduce the padding to make the dialog contents fit into the screen
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // time picker
                DateRangePicker(
                    datePickerState,
                    title={
                        Row (
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                        ){
                            Button(onClick = {onDismiss.invoke()}) {
                                Text(text = "Done")
                            }
                        }
                    }
                )

                // buttons
                Row(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center // place buttons at the center
                ) {
                    // dismiss button
                    TextButton(onClick = { onDismiss.invoke() }) {
                        Text(text = "Dismiss")
                    }

                    // confirm button
                    TextButton(
                        onClick = {
                            onDismiss.invoke()
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}


@Composable
private fun HomeBody(
    onEvent: (AppointmentEvent) -> Unit,
    itemList: List<Event>, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier,onDeleteEvent: (Event) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        if (itemList.isEmpty()) {
            Text(
                text = "Please click on Add Event to add event",
                textAlign = TextAlign.Center,
            )
        } else {
            InventoryList(
                itemList = itemList,
                onItemClick = { onItemClick(it.id) },
                onEvent = onEvent,
                onDeleteEvent = onDeleteEvent
            )
        }
    }

}

@Composable
private fun InventoryList(
    onEvent: (AppointmentEvent) -> Unit,
    itemList: List<Event>, onItemClick: (Event) -> Unit, modifier: Modifier = Modifier,onDeleteEvent: (Event) -> Unit
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items(items = itemList, key = { it.id }) { item ->
            EventCard(
                appointment = item,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onItemClick(item)
                        // Toast.makeText(context, "${item.id} is clicked", Toast.LENGTH_SHORT).show()
                    },
                onEvent = onEvent,
                onDeleteEvent = onDeleteEvent
            )
        }
    }
}


@Composable
 fun EventCard(
    modifier: Modifier = Modifier,
    appointment: Event,
    onEvent: (AppointmentEvent) -> Unit,
    onDeleteEvent: (Event) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(modifier = Modifier) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = modifier
                        .weight(1f)
                ) {
                    Text(text = appointment.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = appointment.description,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                    )
                }

                Canvas(
                    modifier = Modifier
                        .size(size = 20.dp)
                ) {
                    drawCircle(
                        color = colors[appointment.color]
                    )
                }

                IconButton(onClick = {
                    onDeleteEvent.invoke(appointment)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete contact"
                    )
                }

                //export button here
                ExportIconButton(
                    onClick = {
                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/
                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/
                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/

                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/
                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/
                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/

                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/

                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/

                        //In order to understand this you need to remove the coroutine scope and try to
                        // run the app without using the coroutineScope. You will notice Huge performance
                        // difference

                        coroutineScope.launch(Dispatchers.Main) {
                            exportEventToICS(context, appointment)

                        }
                    },
                    contentDescription = "Export appointment"
                )
            }

            Text(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                text = "${appointment.startTime.toDateTime()} - ${appointment.endTime.toDateTime()}"
            , textAlign = TextAlign.End,
                fontSize = 10.sp)
        }
    }
}

fun Long.toDateTime():String{
return  SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(Date(this))
}

@Composable
 fun ExportIconButton(
    onClick: () -> Unit,
    contentDescription: String
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.export),
            contentDescription = contentDescription,
            modifier = Modifier.size(25.dp)
        )
    }
}

private fun exportEventToICS(context: Context, event: Event) {
    val fileName = event.title.plus(".ics")
    val dir = File(context.filesDir, "AppointmentExport")

    if (!dir.exists()) {
        dir.mkdir()
    }

    val file = File(dir, fileName)
    val outputStream = FileOutputStream(file)
    val icsContent = buildICSContent(event)

    try {
        outputStream.write(icsContent.toByteArray())
        outputStream.close()
        Toast.makeText(context, "Event exported to $fileName", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error exporting event", Toast.LENGTH_SHORT).show()
    }
}

private fun buildICSContent(event: Event): String {
//    val cal = Calendar.getInstance()
//    val dateFormat = "yyyyMMdd'T'HHmmss'Z'"
//    val startTime = event.startTime.format(dateFormat)
//    val endTime = event.endTime.format(dateFormat)

    // This is the ics file content and you can adjust it accordingly
    val icsContent = """
        BEGIN:VCALENDAR
        VERSION:2.0
        PRODID:-//My Calendar//EN
        BEGIN:VEVENT
        SUMMARY:${event.title}
        DESCRIPTION:${event.description}
        END:VEVENT
        END:VCALENDAR
    """.trimIndent()

    return icsContent
}





