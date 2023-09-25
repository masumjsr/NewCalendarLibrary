package com.example.newcalendarlibrary.room.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "nameValue") val nameValue: String,
    @ColumnInfo(name = "loginValue") val loginValue: String,
    @ColumnInfo(name = "passwordValue") val passwordValue: String
)
