package com.example.habittracker.feature_habits.di.modules

import com.example.habittracker.core_api.domain.repositories.SyncHabitRepository
import com.example.habittracker.feature_habit_filter_api.di.mediators.HabitFilterMediator
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
    fun bindSyncHabitRepository(impl: SyncHabitRepositoryImpl): SyncHabitRepository

    companion object {

        @Provides
        fun provideHabitFilterMediator(map: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>): HabitFilterMediator =
            map[HabitFilterMediator::class.java]?.get() as? HabitFilterMediator
                ?: throw RuntimeException("HabitFilterMediator is absent into map")
    }
}