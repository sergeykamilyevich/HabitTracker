package com.example.habittracker.di.modules

import com.example.habittracker.data.db.DbHabitRepositoryImpl
import com.example.habittracker.data.network.NetworkHabitRepositoryImpl
import com.example.habittracker.domain.repositories.DbHabitRepository
import com.example.habittracker.domain.repositories.NetworkHabitRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataAbstractModule {

    @[Binds Singleton]
    fun bindDbHabitRepository(impl: DbHabitRepositoryImpl): DbHabitRepository

    @[Binds Singleton]
    fun bindNetworkHabitRepository(impl: NetworkHabitRepositoryImpl): NetworkHabitRepository
}