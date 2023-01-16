package com.example.habittracker.viewmodels_api.di.providers

import com.example.habittracker.viewmodels_api.presentation.tools.Resources
import com.example.habittracker.viewmodels_api.presentation.view_models.ViewModelFactory
import javax.inject.Provider

interface ViewModelsComponentProvider {

    fun mediatorsMap(): Map<Class<*>, @JvmSuppressWildcards Provider<Any>>

    fun provideResources(): Resources

    fun provideViewModelFactory(): ViewModelFactory
}