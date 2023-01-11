package com.example.habittracker.db_api.di.providers

import com.example.habittracker.core_api.domain.repositories.DbHabitRepository

interface DbComponentProvider {

    fun provideDbHabitRepository(): DbHabitRepository

//    fun provideApiService(): HabitApi
//
//    fun provideCloudHabitRepository(): CloudHabitRepository
//
//    fun provideIoErrorFlow(): IoErrorFlow
}