package com.example.habittracker.network_api.di.providers

import com.example.habittracker.core_api.domain.errors.IoErrorFlow
import com.example.habittracker.core_api.domain.repositories.CloudHabitRepository

interface NetworkComponentProvider {

    fun provideApiService(): HabitApi

    fun provideCloudHabitRepository(): CloudHabitRepository

    fun provideIoErrorFlow(): IoErrorFlow
}