package com.example.habittracker.di.modules

import com.example.habittracker.data.repositories.DbHabitRepositoryImpl
import com.example.habittracker.data.repositories.NetworkHabitRepositoryImpl
import com.example.habittracker.data.repositories.SyncHabitRepositoryImpl
import com.example.habittracker.domain.repositories.DbHabitRepository
import com.example.habittracker.domain.repositories.NetworkHabitRepository
import com.example.habittracker.domain.repositories.SyncHabitRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataAbstractModule {

    @[Binds Singleton]
    fun bindDbHabitRepository(impl: DbHabitRepositoryImpl): DbHabitRepository

    @[Binds Singleton]
    fun bindNetworkHabitRepository(impl: NetworkHabitRepositoryImpl): NetworkHabitRepository

    @[Binds Singleton]
    fun bindSyncHabitRepository(impl: SyncHabitRepositoryImpl): SyncHabitRepository
}