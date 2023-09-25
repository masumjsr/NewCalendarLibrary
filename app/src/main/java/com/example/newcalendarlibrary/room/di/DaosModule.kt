package com.example.newcalendarlibrary.room.di

import com.example.newcalendarlibrary.room.AppDatabase
import com.example.newcalendarlibrary.room.events.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun provideEventDao(
        database: AppDatabase
    ): EventDao = database.eventDao()

}