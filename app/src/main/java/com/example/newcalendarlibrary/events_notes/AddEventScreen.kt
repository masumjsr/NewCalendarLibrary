package com.example.newcalendarlibrary.events_notes

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.EventViewModel
import com.example.newcalendarlibrary.color_picker.ColorViewModel
import com.example.newcalendarlibrary.color_picker.ColourButton
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.ui.viewmodel.AddEventViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

//@Preview
//@Composable
//fun EventScreenPreview() {
//    AddEventScreen()
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    modifier: Modifier = Modifier,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    eventViewModel: AddEventViewModel = hiltViewModel(),

    ) {
    Surface(modifier = modifier.fillMaxSize()) {

        val cal=Calendar.getInstance()

        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
        val timePickerState = rememberTimePickerState(cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE))
        val endTimePickerState = rememberTimePickerState()
        val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())


        var showDatePicker by remember {
            mutableStateOf(false)
        }
        var showEndDatePicker by remember {
            mutableStateOf(false)
        }

        var showTimePicker by remember {
            mutableStateOf(false)
        }

        var showEndTimePicker by remember {
            mutableStateOf(false)
        }

        var selectedDate by remember {
            mutableLongStateOf(datePickerState.selectedDateMillis?:0) // or use mutableStateOf(calendar.timeInMillis)
        }

        var selectedEndDate by remember {
            mutableLongStateOf(datePickerState.selectedDateMillis?:0) // or use mutableStateOf(calendar.timeInMillis)
        }

        var selectedTime by remember {
            mutableLongStateOf(cal.timeInMillis)
        }
        var selectedEndTime by remember {
            mutableLongStateOf(cal.timeInMillis)
        }
      DatePicker(showDatePicker, datePickerState){
            showDatePicker=false
            selectedDate=datePickerState.selectedDateMillis?:0
        }



        DatePicker(showEndDatePicker, endDatePickerState){
            showEndDatePicker=false
            selectedEndDate=endDatePickerState.selectedDateMillis?:0
        }

        CustomTimePicker(showDatePicker = showTimePicker, datePickerState =timePickerState ) {
            cal.set(Calendar.HOUR_OF_DAY,timePickerState.hour)
            cal.set(Calendar.MINUTE,timePickerState.minute)
            selectedTime=cal.timeInMillis
            showTimePicker=false

        }
        CustomTimePicker(showDatePicker = showEndTimePicker, datePickerState =endTimePickerState ) {
            cal.set(Calendar.HOUR_OF_DAY,timePickerState.hour)
            cal.set(Calendar.MINUTE,timePickerState.minute)
            selectedEndTime=cal.timeInMillis
            showEndTimePicker=false


        }



//        var title by remember { mutableStateOf("") }
//        val description = remember { mutableStateOf("") }

        val currentDayCal = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val context = LocalContext.current

        // Declaring integer values
        // for year, month and day
        val mYear: Int
        val mMonth: Int
        val mDay: Int

        // Initializing a Calendar
        val mCalendar = Calendar.getInstance()

        // Fetching current year, month and day
        mYear = mCalendar.get(Calendar.YEAR)
        mMonth = mCalendar.get(Calendar.MONTH)
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

        mCalendar.time = Date()

        // Declaring a string value to
        // store date in string format
        var mDate = remember { mutableStateOf("") }

        // Declaring DatePickerDialog and setting
        // initial values as current values (present year, month and day)
        val mDatePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
            }, mYear, mMonth, mDay
        )

        val mHour = mCalendar[Calendar.HOUR_OF_DAY]
        val mMinute = mCalendar[Calendar.MINUTE]

        // Value for storing time as a string
        val mTime = remember { mutableStateOf("") }

        // Creating a TimePicker dialod
        val mTimePickerDialog = TimePickerDialog(
            context,
            { _, mHour: Int, mMinute: Int ->
                mTime.value = "$mHour:$mMinute"
            }, mHour, mMinute, false
        )

        var title by remember {
            mutableStateOf("")
        }
        var description by remember {
            mutableStateOf("")
        }

        var selectedColor by remember {
            mutableIntStateOf(0)
        }

        androidx.compose.material.AlertDialog(
            onDismissRequest = { onEvent(AppointmentEvent.HideDialog) },
           // title = { Text(text = "Add Event")},
            shape = RoundedCornerShape(12.dp),
            text = {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title=it },
                       // label = { Text(text = "Title") },
                        placeholder = {
                            Text(
                                text = "Title", color = MaterialTheme.colors.onBackground
                            )
                        })
                    Spacer(modifier = modifier.padding(vertical = 5.dp))
                Text("From")
                    Row (
                        modifier= Modifier.fillMaxWidth(),
                        horizontalArrangement =Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = {showDatePicker=true}) {
                            Text(text = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(selectedDate)))
                        }
                        TextButton(onClick = {showTimePicker=true}) {
                            Text(text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(selectedTime)))

                        }
                    }
                    
                    Spacer(modifier = modifier.padding(vertical = 5.dp))

                    Text("To")
                    Row (
                        modifier= Modifier.fillMaxWidth(),
                        horizontalArrangement =Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = {showEndDatePicker=true

                            Log.i("123321", "AddEventScreen: end date selected")
                        }) {
                            Text(text = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(selectedEndDate)))
                        }
                        TextButton(onClick = {showEndTimePicker=true}) {
                            Text(text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(selectedEndTime)))

                        }
                    }

                    Spacer(modifier = modifier.padding(vertical = 5.dp))


                    ColourButton(
                        colors = colors,
                        onColorSelected = {
                             selectedColor=it
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                    )
                    Spacer(modifier = modifier.padding(vertical = 5.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description=it },
                        label = { Text("Description") },
                        singleLine = false,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start
                        ),
                        modifier = modifier.height(150.dp).fillMaxWidth(),
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
                        
                        val calendar=Calendar.getInstance()
                        calendar.timeInMillis=datePickerState.selectedDateMillis?:System.currentTimeMillis()
                        calendar.set(Calendar.HOUR_OF_DAY,timePickerState.hour)
                        calendar.set(Calendar.MINUTE,timePickerState.minute)

                        val endCalendar=Calendar.getInstance()
                        endCalendar.timeInMillis=endDatePickerState.selectedDateMillis?:System.currentTimeMillis()
                        endCalendar.set(Calendar.HOUR_OF_DAY,endTimePickerState.hour)
                        endCalendar.set(Calendar.MINUTE,endTimePickerState.minute)

                        eventViewModel.storeEvent(
                            Event(
                                id=0,
                                title = title,
                                description=description,
                                startTime = calendar.timeInMillis,
                                endTime = endCalendar.timeInMillis,
                                color = selectedColor,
                                user = eventViewModel.user
                            )
                        )
                        onEvent(AppointmentEvent.HideDialog)
                    }) {
                        Text(text = "Save")
                    }

                }
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DatePicker(
    showDatePicker: Boolean,
    datePickerState: DatePickerState,
    onDismiss:()->Unit
) {
    var showDatePicker1 = showDatePicker
    if (showDatePicker1) {
        DatePickerDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                showDatePicker1 = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss.invoke()

                    },
                    enabled = true
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker1 = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}



@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CustomTimePicker(
    showDatePicker: Boolean,
    datePickerState: TimePickerState,
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
                TimePicker(
                    datePickerState
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
fun OutlinedTextFieldMessage(
    modifier: Modifier,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit
) {

}