package com.example.habittracker.data.di.modules

import android.app.Application
import com.example.habittracker.data.db.room.AppDataBase
import com.example.habittracker.data.db.room.HabitDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {

    @[Provides Singleton]
    fun provideHabitDao(application: Application): HabitDao = AppDataBase.getInstance(application).habitDao()

}