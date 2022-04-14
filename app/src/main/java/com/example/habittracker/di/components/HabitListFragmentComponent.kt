package com.example.habittracker.di.components

import com.example.habittracker.presentation.ui.HabitListFragment
import dagger.Subcomponent

//import com.example.habittracker.di.HabitListFragmentScope
//import com.example.habittracker.di.MainActivityScope
//import com.example.habittracker.presentation.ui.HabitItemFragment
//import com.example.habittracker.presentation.ui.HabitListFragment
//import dagger.Subcomponent
//
////@HabitListFragmentScope
@Subcomponent
interface HabitListFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HabitListFragmentComponent
    }

//    fun habitListViewModelComponent(): HabitListViewModelComponent.Factory

    fun inject(habitListFragment: HabitListFragment)
}