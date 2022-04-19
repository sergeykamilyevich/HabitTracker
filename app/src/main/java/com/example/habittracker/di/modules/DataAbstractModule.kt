package com.example.habittracker.di.modules

import com.example.habittracker.data.db.DbHabitRepositoryImpl
import com.example.habittracker.domain.repositories.DbHabitRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataAbstractModule {

    @[Binds Singleton]
    fun bindHabitRepository(impl: DbHabitRepositoryImpl): DbHabitRepository
}