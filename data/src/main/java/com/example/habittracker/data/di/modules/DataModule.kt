package com.example.habittracker.data.di.modules

import android.app.Application
import com.example.habittracker.data.db.room.AppDataBase
import com.example.habittracker.data.db.room.HabitDao
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
import dagger.Provides

@Module
interface DataModule {

    @[Binds]
    fun bindDbHabitRepository(impl: DbHabitRepositoryImpl): DbHabitRepository

    @[Binds]
    fun bindNetworkHabitRepository(impl: CloudHabitRepositoryImpl): CloudHabitRepository

    @[Binds]
    fun bindSyncHabitRepository(impl: SyncHabitRepositoryImpl): SyncHabitRepository

    @[Binds]
    fun bindIoErrorFlow(impl: IoErrorFlowImpl): IoErrorFlow

    companion object {

        @[Provides]
        fun provideHabitDao(application: Application): HabitDao =
            AppDataBase.getInstance(application).habitDao()
    }
}