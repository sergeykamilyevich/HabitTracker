package com.example.habittracker.di.components

import com.example.habittracker.di.MainActivityScope
import com.example.habittracker.di.modules.HabitListViewModelModule
import com.example.habittracker.presentation.ui.BottomSheetFragment
import com.example.habittracker.presentation.ui.HabitListFragment
import com.example.habittracker.presentation.ui.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent

@MainActivityScope
@Subcomponent(modules = [HabitListViewModelModule::class])
interface MainActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: MainActivity): MainActivityComponent
    }

    fun inject(bottomSheetFragment: BottomSheetFragment)

    fun inject(habitListFragment: HabitListFragment)

}