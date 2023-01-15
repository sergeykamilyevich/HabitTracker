package com.example.habittracker.feature_habits.di.modules

import com.example.habittracker.feature_habits.presentation.view_models.Resources
import com.example.habittracker.viewmodels.presentation.FilterViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
interface FeatureHabitsModule {

    @[Binds]
    fun bindResources(impl: Resources.Base): Resources

    companion object {

        @Provides
        fun provideFilterViewModel(map: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>):
                FilterViewModel =
            map[FilterViewModel::class.java]?.get() as? FilterViewModel
                ?: throw RuntimeException("FilterViewModel is absent into map")
    }

}