package com.example.habittracker.feature_habit_filter.di.modules

import com.example.habittracker.viewmodels.presentation.FilterViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
interface FeatureHabitFilterModule {

    companion object {

        @Provides
        fun provideFilterViewModelImpl(map: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>):
        FilterViewModel =
            map[FilterViewModel::class.java]?.get() as? FilterViewModel
                ?: throw RuntimeException("FilterViewModel is absent into map")

    }
}