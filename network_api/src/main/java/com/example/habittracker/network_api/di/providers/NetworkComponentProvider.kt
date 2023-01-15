package com.example.habittracker.network_api.di.providers

import com.example.habittracker.core_api.data.store.UserPreferences
import com.example.habittracker.core_api.domain.errors.IoErrorFlow
import com.example.habittracker.network_api.domain.repositories.CloudHabitRepository
import com.example.habittracker.network_api.domain.usecases.CloudUseCase

interface NetworkComponentProvider {

    fun provideApiService(): HabitApi

    fun provideCloudHabitRepository(): CloudHabitRepository

    fun provideIoErrorFlow(): IoErrorFlow

    fun provideCloudUseCase(): CloudUseCase

    fun provideUserPreferences(): UserPreferences
}