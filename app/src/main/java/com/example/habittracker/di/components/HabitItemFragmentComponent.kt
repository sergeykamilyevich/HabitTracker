package com.example.habittracker.di.components

import com.example.habittracker.di.HabitItemFragmentScope
import com.example.habittracker.di.modules.HabitItemViewModelModule
import com.example.habittracker.presentation.ui.HabitItemFragment
import dagger.BindsInstance
import dagger.Subcomponent

@HabitItemFragmentScope
@Subcomponent(modules = [HabitItemViewModelModule::class])
interface HabitItemFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: HabitItemFragment): HabitItemFragmentComponent
    }

    fun inject(habitItemFragment: HabitItemFragment)

}



