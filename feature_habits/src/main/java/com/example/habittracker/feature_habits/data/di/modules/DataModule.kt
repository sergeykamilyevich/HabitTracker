package com.example.habittracker.feature_habits.data.di.modules

import android.app.Application
import com.example.habittracker.core.domain.repositories.DbHabitRepository
import com.example.habittracker.feature_habits.data.db.room.AppDataBase
import com.example.habittracker.feature_habits.data.db.room.HabitDao
import com.example.habittracker.feature_habits.data.repositories.DbHabitRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[Binds]
    fun bindDbHabitRepository(impl: DbHabitRepositoryImpl): DbHabitRepository

//    @[Binds]
//    fun bindNetworkHabitRepository(impl: CloudHabitRepositoryImpl): CloudHabitRepository

//    @[Binds]
//    fun bindSyncHabitRepository(impl: com.example.habittracker.feature_sync.repositories.SyncHabitRepositoryImpl): SyncHabitRepository

//    @[Binds]
//    fun bindIoErrorFlow(impl: IoErrorFlowImpl): IoErrorFlow

    companion object {

        @[Provides]
        fun provideHabitDao(application: Application): HabitDao =
            AppDataBase.getInstance(application).habitDao()
    }
}