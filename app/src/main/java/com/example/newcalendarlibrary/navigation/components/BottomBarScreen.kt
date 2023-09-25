package com.example.newcalendarlibrary.navigation.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.newcalendarlibrary.R
sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val icon_focused: Int
) {

    // for home
    object Home: BottomBarScreen(
        route = "home_screen",
        title = "Home",
        icon = R.drawable.baseline_home_24,
        icon_focused = R.drawable.baseline_home_24

    )

    object Calendar: BottomBarScreen(
        route = "calendar_screen",
        title = "Explore",
        icon = R.drawable.baseline_calendar_month_24,
        icon_focused = R.drawable.baseline_calendar_month_24
    )


    object SettingsScreen: BottomBarScreen(
        route = "settings_screen",
        title = "Settings",
        icon = R.drawable.baseline_settings_24,
        icon_focused = R.drawable.baseline_settings_24
    )



}