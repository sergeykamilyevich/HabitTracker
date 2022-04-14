package com.example.habittracker.di

import javax.inject.Scope

@Scope
annotation class AppScope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class MainActivityScope

@Scope
annotation class HabitItemViewModelScope

@Scope
annotation class HabitListViewModelScope

@Scope
annotation class HabitItemFragmentScope

@Scope
annotation class HabitListFragmentScope