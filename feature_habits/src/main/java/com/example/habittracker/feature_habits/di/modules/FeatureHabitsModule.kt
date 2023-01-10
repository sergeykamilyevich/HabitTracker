package com.example.habittracker.feature_habits.di.modules

import com.example.habittracker.core.domain.repositories.SyncHabitRepository
import com.example.habittracker.feature_habits.presentation.view_models.Resources
import com.example.habittracker.feature_habits.data.repositories.SyncHabitRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface FeatureHabitsModule {

    @[Binds]
    fun bindResources(impl: Resources.Base): Resources

    @[Binds]
    fun bindSyncHabitRepository(impl: SyncHabitRepositoryImpl): SyncHabitRepository
}