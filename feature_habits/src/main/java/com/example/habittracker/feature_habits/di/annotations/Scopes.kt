package com.example.habittracker.feature_habits.di.annotations

import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class HabitItemViewModelScope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class MainViewModelScope
