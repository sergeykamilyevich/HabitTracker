package com.example.habittracker.di.annotations

import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class HabitItemViewModelScope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class MainViewModelScope
