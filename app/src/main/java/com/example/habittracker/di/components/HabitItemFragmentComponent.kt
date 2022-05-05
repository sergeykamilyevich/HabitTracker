package com.example.habittracker.di.components

import com.example.habittracker.di.annotations.HabitItemViewModelScope
import com.example.habittracker.presentation.ui.HabitItemFragment
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