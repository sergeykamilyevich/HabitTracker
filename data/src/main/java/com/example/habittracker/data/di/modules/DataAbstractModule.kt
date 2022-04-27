package com.example.habittracker.data.di.modules

import com.example.habittracker.data.network.retrofit.IoErrorFlowImpl
import com.example.habittracker.data.repositories.CloudHabitRepositoryImpl
import com.example.habittracker.data.repositories.DbHabitRepositoryImpl
import com.example.habittracker.data.repositories.SyncHabitRepositoryImpl
import com.example.habittracker.domain.errors.IoErrorFlow
import com.example.habittracker.domain.repositories.CloudHabitRepository
import com.example.habittracker.domain.repositories.DbHabitRepository
import com.example.habittracker.domain.repositories.SyncHabitRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataAbstractModule {

    @[Binds Singleton]
    fun bindDbHabitRepository(impl: DbHabitRepositoryImpl): DbHabitRepository

    @[Binds Singleton]
    fun bindNetworkHabitRepository(impl: CloudHabitRepositoryImpl): CloudHabitRepository

    @[Binds Singleton]
    fun bindSyncHabitRepository(impl: SyncHabitRepositoryImpl): SyncHabitRepository

    @[Binds Singleton]
    fun bindIoErrorFlow(impl: IoErrorFlowImpl): IoErrorFlow
}