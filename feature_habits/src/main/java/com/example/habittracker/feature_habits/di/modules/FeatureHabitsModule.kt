package com.example.habittracker.feature_habits.di.modules

import com.example.habittracker.viewmodels_impl.presentation.view_models.FilterViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
interface FeatureHabitsModule {

    companion object {

        @Provides
        fun provideFilterViewModel(map: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>):
                FilterViewModel =
            map[FilterViewModel::class.java]?.get() as? FilterViewModel
                ?: throw RuntimeException("FilterViewModel is absent into map")
    }

}