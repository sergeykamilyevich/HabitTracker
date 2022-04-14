package com.example.habittracker.di.modules

import com.example.habittracker.data.db.HabitRepositoryImpl
import com.example.habittracker.domain.repositories.HabitRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataAbstractModule {

    @[Binds Singleton]
    fun bindHabitRepository(impl: HabitRepositoryImpl): HabitRepository
}