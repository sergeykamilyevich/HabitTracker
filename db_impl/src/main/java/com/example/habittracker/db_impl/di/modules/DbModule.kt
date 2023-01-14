package com.example.habittracker.db_impl.di.modules

import android.content.Context
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.data.db.room.HabitDao
import com.example.habittracker.db_api.domain.usecases.*
import com.example.habittracker.db_impl.data.db.room.AppDataBase
import com.example.habittracker.db_impl.di.repositories.DbHabitRepositoryImpl
import com.example.habittracker.db_impl.domain.usecases.*
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DbModule {

    @Binds
    fun bindDbHabitRepository(impl: DbHabitRepositoryImpl): DbHabitRepository

    @Binds
    fun bindDbUseCase(impl: DbUseCaseImpl): DbUseCase

    @Binds
    fun bindAddHabitDoneUseCase(impl: AddHabitDoneUseCaseImpl): AddHabitDoneUseCase

    @Binds
    fun bindDeleteHabitDoneUseCase(impl: DeleteHabitDoneUseCaseImpl): DeleteHabitDoneUseCase

    @Binds
    fun bindDeleteHabitUseCase(impl: DeleteHabitUseCaseImpl): DeleteHabitUseCase

    @Binds
    fun bindGetHabitUseCase(impl: GetHabitUseCaseImpl): GetHabitUseCase

    @Binds
    fun bindGetHabitListUseCase(impl: GetHabitListUseCaseImpl): GetHabitListUseCase

    @Binds
    fun bindGetUnfilteredHabitListUseCase(impl: GetUnfilteredHabitListUseCaseImpl): GetUnfilteredHabitListUseCase

    @Binds
    fun bindUpsertHabitUseCase(impl: UpsertHabitUseCaseImpl): UpsertHabitUseCase

    @Binds
    fun bindDeleteAllHabitsUseCase(impl: DeleteAllHabitsUseCaseImpl): DeleteAllHabitsUseCase

    companion object {

        @[ApplicationScope Provides]
        fun provideHabitDao(context: Context): HabitDao =
            AppDataBase.getInstance(context = context).habitDao()
    }

}