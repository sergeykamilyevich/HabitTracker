package com.example.habittracker.feature_habit_filter.di.modules

import com.example.habittracker.viewmodels_api.presentation.view_models.FilterViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
interface FeatureHabitFilterModule {

    companion object {

        @Provides
        fun provideFilterViewModel(map: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>):
                FilterViewModel =
            map[FilterViewModel::class.java]?.get() as? FilterViewModel
                ?: throw RuntimeException("FilterViewModel is absent into map")

    }
}