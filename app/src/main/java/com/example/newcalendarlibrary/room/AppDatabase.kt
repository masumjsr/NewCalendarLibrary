package com.example.newcalendarlibrary.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.events.EventDao
import com.example.newcalendarlibrary.room.notes.Note
import com.example.newcalendarlibrary.room.notes.NoteDAO
import com.example.newcalendarlibrary.room.user.User
import com.example.newcalendarlibrary.room.user.UserDAO

@Database(entities = [User::class, Note::class,Event::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDAO
    abstract fun userDao(): UserDAO
    abstract fun eventDao(): EventDao

    object AppDb {
        private var db: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (db == null) {
                db = Room.databaseBuilder(context, AppDatabase::class.java,"AppDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return db!!
        }
    }
}