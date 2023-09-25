package com.example.newcalendarlibrary.register

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.newcalendarlibrary.MainActivity
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.events_notes.NotesViewModel
import com.example.newcalendarlibrary.navigation.Graph
import com.example.newcalendarlibrary.navigation.Screens
import com.example.newcalendarlibrary.navigation.components.BottomBarScreen
import com.example.newcalendarlibrary.ui.theme.primaryColor
import com.example.newcalendarlibrary.ui.theme.textWhiteColor
import com.example.newcalendarlibrary.ui.viewmodel.MyUserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

private const val TAG = "Login"
@SuppressLint("SuspiciousIndentation")
@Composable
fun LoginPage(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    notesViewModel: NotesViewModel = viewModel(),
    viewModel: MyUserViewModel = hiltViewModel(),
) {
    val loginState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val dialogOpen = remember { mutableStateOf(false) }
    val dialogOpen1 = remember { mutableStateOf(false) }

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
        ExitDialog(
            onConfirmExit = {
                dialogOpen.value = false
                val activity = MainActivity()
                activity.finish()
                exitProcess(0)
            },
            onDismiss = {
                dialogOpen.value = false
            }
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.BottomCenter
    ) {


        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .background(Color.White)
        ) {
            item {
                Text(
                    text = "Login",
                    fontSize = 30.sp,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = loginState.value,
                    onValueChange = {
                        loginState.value = it
                        userViewModel.logout()
                    },
                    label = { Text(text = "Login name") },
                    placeholder = { Text(text = "Login name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.88f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = {
                        passwordState.value = it
                        userViewModel.logout()
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility.value = !passwordVisibility.value
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.password_hide),
                                contentDescription = null,
                                tint = if (passwordVisibility.value) primaryColor else Color.Gray
                            )
                        }
                    },
                    label = { Text("Password") },
                    placeholder = { Text(text = "Password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(0.88f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.padding(10.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                    onClick = {
                        userViewModel.logout()
                        val userExists = runBlocking {
                            userViewModel.checkIfUserExists(loginState.value)
                        }
                        if (userExists) {
                            val validPassword = runBlocking {
                                userViewModel.checkPassword(
                                    loginState.value,
                                    passwordState.value
                                )
                            }
                            if (passwordState.value.isNotEmpty() && validPassword) {
                                runBlocking {
                                    userViewModel.login(
                                        loginState.value,
                                        passwordState.value
                                    )
                                }

                                userViewModel.currentUser.value!!.id?.let {
                                    notesViewModel.reloadNotes(
                                        it
                                    )

                                }
                                viewModel.storeUser(loginState.value)

                              val ctrl =  navController.navigate("${BottomBarScreen.Home.route}/${userViewModel.currentUser.value!!.id}")
                                Log.d(TAG,"Navigation is $ctrl")
                            } else {
                                Toast.makeText(
                                    context,
                                    "Incorrect login or password.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "User doesn't exist.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp)
                        .clip(CircleShape)
                ) {
                    Text(text = "Sign In", fontSize = 20.sp, color = textWhiteColor)
                }

                Spacer(modifier = Modifier.padding(20.dp))

                Text(
                    text = "Create an Account",
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(Screens.SignUpScreen.route) {
                            launchSingleTop = true
                        }
                    })
                )

                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}

@Composable
fun ExitDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Exit", fontWeight = FontWeight.Bold)
        },
        text = {
            Text("Are you sure you want to exit?", fontWeight = FontWeight.SemiBold)
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                onClick = onConfirmExit
            ) {
                Text("Exit", color = Color.White)
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