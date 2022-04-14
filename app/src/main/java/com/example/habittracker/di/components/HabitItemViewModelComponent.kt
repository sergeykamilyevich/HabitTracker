package com.example.habittracker.di.components

import com.example.habittracker.di.HabitItemViewModelScope
import com.example.habittracker.di.modules.HabitItemViewModelModule
import com.example.habittracker.presentation.ui.HabitItemFragment
import dagger.Subcomponent

//@HabitItemViewModelScope
@Subcomponent(modules = [HabitItemViewModelModule::class])
interface HabitItemViewModelComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HabitItemViewModelComponent
    }

    fun inject(habitItemFragment: HabitItemFragment)

}



