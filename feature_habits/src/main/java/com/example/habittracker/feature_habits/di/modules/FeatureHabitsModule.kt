package com.example.habittracker.feature_habits.di.modules

import com.example.habittracker.cloud_sync.domain.repositories.SyncHabitRepository
import com.example.habittracker.viewmodels.presentation.FilterViewModel
import com.example.habittracker.feature_habits.data.repositories.SyncHabitRepositoryImpl
import com.example.habittracker.feature_habits.presentation.view_models.Resources
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
interface FeatureHabitsModule {

    @[Binds]
    fun bindResources(impl: Resources.Base): Resources

    @[Binds]
    fun bindSyncHabitRepository(impl: SyncHabitRepositoryImpl): SyncHabitRepository //TODO remove to sync module

    companion object {

        @Provides
        fun provideFilterViewModel(map: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>):
                FilterViewModel =
            map[FilterViewModel::class.java]?.get() as? FilterViewModel
                ?: throw RuntimeException("FilterViewModel is absent into map")
    }

}