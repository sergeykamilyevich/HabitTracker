package com.example.habittracker.db_impl.di.modules

import android.content.Context
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.data.db.room.HabitDao
import com.example.habittracker.db_api.domain.usecases.*
import com.example.habittracker.db_impl.data.db.room.AppDataBase
import com.example.habittracker.db_impl.di.repositories.DbHabitRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DbModule {

    @Binds
    fun bindDbHabitRepository(impl: DbHabitRepositoryImpl): DbHabitRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideHabitDao(context: Context): HabitDao =
            AppDataBase.getInstance(context = context).habitDao()

        @Provides
        fun provideAddHabitDoneUseCase(dbHabitRepository: DbHabitRepository): AddHabitDoneUseCase =
            AddHabitDoneUseCase(dbHabitRepository::addHabitDone)

        @Provides
        fun provideDeleteAllHabitsUseCase(dbHabitRepository: DbHabitRepository): DeleteAllHabitsUseCase =
            DeleteAllHabitsUseCase(dbHabitRepository::deleteAllHabits)

        @Provides
        fun provideDeleteHabitDoneUseCase(dbHabitRepository: DbHabitRepository): DeleteHabitDoneUseCase =
            DeleteHabitDoneUseCase(dbHabitRepository::deleteHabitDone)

        @Provides
        fun provideDeleteHabitUseCase(dbHabitRepository: DbHabitRepository): DeleteHabitUseCase =
            DeleteHabitUseCase(dbHabitRepository::deleteHabit)

        @Provides
        fun provideGetHabitListUseCase(dbHabitRepository: DbHabitRepository): GetHabitListUseCase =
            GetHabitListUseCase(dbHabitRepository::getHabitList)

        @Provides
        fun provideGetHabitUseCase(dbHabitRepository: DbHabitRepository): GetHabitUseCase =
            GetHabitUseCase(dbHabitRepository::getHabitById)

        @Provides
        fun provideGetUnfilteredHabitListUseCase(dbHabitRepository: DbHabitRepository): GetUnfilteredHabitListUseCase =
            GetUnfilteredHabitListUseCase(dbHabitRepository::getUnfilteredList)

        @Provides
        fun provideUpsertHabitUseCase(dbHabitRepository: DbHabitRepository): UpsertHabitUseCase =
            UpsertHabitUseCase(dbHabitRepository::upsertHabit)
    }

}