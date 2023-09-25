package com.example.newcalendarlibrary.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

val PREF_TAG="user"
val SETTING_TAG="setting"
@Singleton
class MyPreference @Inject constructor(@ApplicationContext context : Context){
    val prefs = context.getSharedPreferences("config",MODE_PRIVATE)

    fun getUser(): String {
        return prefs.getString(PREF_TAG, "")!!
    }
    fun setUser(query: String) {
        prefs.edit().putString(PREF_TAG, query).apply()
    }

    fun getSetting(): Int {
        return prefs.getInt(SETTING_TAG, 0)!!
    }
    fun setSetting(setting: Int) {
        prefs.edit().putInt(SETTING_TAG, setting).apply()
    }

    }
