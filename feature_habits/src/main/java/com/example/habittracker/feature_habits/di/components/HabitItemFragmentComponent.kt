package com.example.habittracker.feature_habits.di.components

import com.example.habittracker.feature_habits.di.annotations.HabitItemViewModelScope
import com.example.habittracker.feature_habits.presentation.ui.HabitItemFragment
import dagger.Subcomponent

@HabitItemViewModelScope
@Subcomponent
interface HabitItemFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HabitItemFragmentComponent
    }

    fun inject(habitItemFragment: HabitItemFragment)

}