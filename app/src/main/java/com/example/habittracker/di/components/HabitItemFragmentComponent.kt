package com.example.habittracker.di.components

import com.example.habittracker.di.HabitItemFragmentScope
import com.example.habittracker.di.MainActivityScope
import com.example.habittracker.presentation.ui.HabitItemFragment
import dagger.Subcomponent

//@HabitItemFragmentScope
@Subcomponent
interface HabitItemFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HabitItemFragmentComponent
    }

//    fun habitItemViewModelComponent(): HabitItemViewModelComponent.Factory

    fun inject(habitItemFragment: HabitItemFragment)
}