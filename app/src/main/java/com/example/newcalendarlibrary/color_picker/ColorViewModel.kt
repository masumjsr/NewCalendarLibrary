package com.example.newcalendarlibrary.color_picker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class ColorViewModel : ViewModel(){
    var selectedColor by mutableStateOf<Color>(Color(0xFFFFFFFF))
    fun updateSelectedColor(newColor: Color) {
        selectedColor = newColor
    }
}