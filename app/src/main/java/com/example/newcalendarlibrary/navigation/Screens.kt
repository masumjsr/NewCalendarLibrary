package com.example.newcalendarlibrary.navigation

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
}

sealed class Screens(val route: String) {
    object LoginScreen : Screens(route = "login_screen")
    object SignUpScreen : Screens(route = "signup_screen")
    object AddEventScreen : Screens(route = "add_event_screen")
    object EventUpdateScreen : Screens(route = "event_update_screen")

}