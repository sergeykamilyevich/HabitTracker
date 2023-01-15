package com.example.habittracker.db_api.di.providers

import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.domain.usecases.DbUseCase

interface DbComponentProvider {

    fun provideDbHabitRepository(): DbHabitRepository

    fun provideDbUseCase(): DbUseCase

}