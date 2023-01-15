package com.example.habittracker.app

import com.example.habittracker.feature_habit_filter.di.components.FeatureHabitFilterComponentProvider
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponentProvider

interface FeatureComponentsProvider :
    FeatureHabitsComponentProvider,
    FeatureHabitFilterComponentProvider