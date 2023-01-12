package com.example.habittracker.feature_habit_filter_impl.di.modules

import com.example.habittracker.feature_habit_filter_api.di.mediators.HabitFilterMediator
import com.example.habittracker.feature_habit_filter_impl.di.mediators.HabitFilterMediatorImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
interface HabitFilterExternalModule {

    @Binds
    @IntoMap
    @ClassKey(HabitFilterMediator::class)
    fun bindMediator(mediator: HabitFilterMediatorImpl): Any
}