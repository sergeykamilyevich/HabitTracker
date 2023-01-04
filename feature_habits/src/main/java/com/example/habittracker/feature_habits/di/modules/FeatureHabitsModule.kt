package com.example.habittracker.feature_habits.di.modules

import com.example.habittracker.feature_habits.presentation.view_models.Resources
import dagger.Binds
import dagger.Module

@Module
interface FeatureHabitsModule {
    @[Binds]
    fun bindResources(impl: Resources.Base): Resources
}