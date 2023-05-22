package com.example.newcalendarlibrary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newcalendarlibrary.screens.HomeScreen
import com.example.newcalendarlibrary.screens.Screen
import com.example.newcalendarlibrary.screens.TodoList

@Composable
fun Navi() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MainScreen.route){
        composable(route = Screen.MainScreen.route){
            HomeScreen(navController = navController)
        }

        composable(Screen.TodoListScreen.route) {
            TodoList(navController = navController)
        }
    }
}