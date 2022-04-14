package com.example.habittracker.di.modules

import com.example.habittracker.data.db.HabitRepositoryImpl
import com.example.habittracker.domain.repositories.HabitRepository
import dagger.Binds
import dagger.Module

@Module
interface DataAbstractModule {

    @Binds
    fun bindHabitRepository(impl: HabitRepositoryImpl): HabitRepository
}