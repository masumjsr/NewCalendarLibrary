package com.example.newcalendarlibrary.event_update

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.navigation.components.BottomBarScreen
import com.example.newcalendarlibrary.room.events.EventDao
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

@Composable
fun EventUpdateScreen(
    modifier: Modifier = Modifier,
    itemId : Int,
    onEvent: (AppointmentEvent) -> Unit,
    state : AppointmentState,
    navController: NavController,
    eventDao: EventDao
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        val viewModel: EventUpdateViewModel = viewModel(
            factory = EventUpdateViewModelFactory(eventDao, itemId)
        )

        val context = LocalContext.current
        val appointment = state.appointment.first { appointment -> itemId == appointment.id  }
        viewModel.title = appointment.title
        viewModel.description = appointment.description
        val coroutineScope = rememberCoroutineScope()

        AlertDialog(
            onDismissRequest = { onEvent(AppointmentEvent.HideDialog) },
            // title = { Text(text = "Add Event")},
            shape = RoundedCornerShape(12.dp),
            modifier = modifier.wrapContentSize(),
            text = {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    OutlinedTextField(
                        value = viewModel.title,
                        onValueChange = { viewModel.title = it },
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
                        value = viewModel.description,
                        onValueChange = { viewModel.description = it },
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
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        coroutineScope.launch {
                            viewModel.updateEventInDatabase(itemId = itemId)
                        }
                        Toast.makeText(context, "Event Updated", Toast.LENGTH_SHORT).show()
                        navController.navigateUp() // Going back to Calendar screen
                    }) {
                        Text(text = "Update")
                    }

                }
            }
        )
    }
}

@Composable
private fun OutlinedTextFieldMessage(
    modifier: Modifier,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit
) {
    OutlinedTextField(
        value = state.description,
        onValueChange = { onEvent(AppointmentEvent.SetDescription(it)) },
        label = { Text("Description") },
        singleLine = false,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        textStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Start
        ),
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(22.dp),
    )
}

@Composable
fun ItemInputForm(
    appointmentState: AppointmentState,
    modifier: Modifier = Modifier,
    onValueChange: (AppointmentState) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = appointmentState.title,
            onValueChange = { onValueChange(appointmentState.copy(title = it)) },
           // label = { Text(stringResource(R.string.item_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = appointmentState.description,
            onValueChange = { onValueChange(appointmentState.copy(description = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            //label = { Text(stringResource(R.string.item_price_req)) },
            leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}