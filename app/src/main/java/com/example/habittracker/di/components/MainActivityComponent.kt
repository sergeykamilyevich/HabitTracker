package com.example.habittracker.di.components

import android.app.Application
import com.example.habittracker.di.MainActivityScope
import com.example.habittracker.di.modules.HabitListViewModelModule
import com.example.habittracker.domain.usecases.*
import com.example.habittracker.presentation.ui.BottomSheetFragment
import com.example.habittracker.presentation.ui.HabitListFragment
import com.example.habittracker.presentation.ui.MainActivity
import com.example.habittracker.presentation.view_models.HabitListViewModel
import dagger.BindsInstance
import dagger.Provides
import dagger.Subcomponent

@MainActivityScope
@Subcomponent(modules = [HabitListViewModelModule::class])
interface MainActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: MainActivity): MainActivityComponent
    }

    fun inject(activity: MainActivity)
    fun inject(bottomSheetFragment: BottomSheetFragment)
    fun inject(habitListFragment: HabitListFragment)

//    fun habitListFragmentComponent(): HabitListFragmentComponent.Factory


//
//    fun habitItemFragmentComponent(): HabitItemFragmentComponent.Factory
//
//
//    fun bottomSheetFragmentComponent(): BottomSheetFragmentComponent.Factory
}