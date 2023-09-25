package com.example.newcalendarlibrary.calendar

/**
 * Represents the type of a calendar.
 */
sealed interface KalendarType {
    /**
     * Firey calendar type.
     */
    object Firey : KalendarType

    /**
     * Oceanic calendar type.
     */
    object Oceanic : KalendarType
}