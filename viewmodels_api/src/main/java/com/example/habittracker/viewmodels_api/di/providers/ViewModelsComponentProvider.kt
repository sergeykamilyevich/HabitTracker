package com.example.habittracker.viewmodels_api.di.providers

import com.example.habittracker.db_api.domain.usecases.AddHabitDoneUseCase
import com.example.habittracker.db_api.domain.usecases.DbUseCase
import com.example.habittracker.viewmodels_api.presentation.tools.Resources
import javax.inject.Provider

interface ViewModelsComponentProvider {

    fun mediatorsMap(): Map<Class<*>, @JvmSuppressWildcards Provider<Any>>

    fun provideResources(): Resources
}