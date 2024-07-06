package com.example.habittracker.core_api.di.providers

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.habittracker.core_api.data.store.UserPreferences
import com.example.habittracker.core_api.domain.interactor.TimeConvertInteractor

interface CoreComponentProvider: CoreContextProvider {

    fun provideDataStore(): DataStore<Preferences>

    fun provideUserPreferences(): UserPreferences

    fun provideTimeConverterInteractor(): TimeConvertInteractor
}