package com.example.habittracker.db_impl.di.modules

import android.content.Context
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.data.db.room.HabitDao
import com.example.habittracker.db_impl.data.db.room.AppDataBase
import com.example.habittracker.db_impl.di.repositories.DbHabitRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DbModule {

    @[Binds]
    fun bindDbHabitRepository(impl: DbHabitRepositoryImpl): DbHabitRepository

    companion object {

        @[Provides]
        fun provideHabitDao(context: Context): HabitDao =
            AppDataBase.getInstance(context = context).habitDao()
    }

}