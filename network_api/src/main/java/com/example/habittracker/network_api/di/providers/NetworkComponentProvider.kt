package com.example.habittracker.network_api.di.providers

import com.example.habittracker.core.domain.errors.IoErrorFlow
import com.example.habittracker.core.domain.repositories.CloudHabitRepository

interface NetworkComponentProvider {

    fun provideApiService(): HabitApi

    fun provideCloudHabitRepository(): CloudHabitRepository

    fun provideIoErrorFlow(): IoErrorFlow
}