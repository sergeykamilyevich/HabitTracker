package com.example.habittracker.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.di.MainActivityScope
import com.example.habittracker.domain.usecases.*
import com.example.habittracker.presentation.ui.MainActivity
import com.example.habittracker.presentation.view_models.HabitListViewModel
import dagger.Module
import dagger.Provides

@Module
object HabitListViewModelModule {

    @[Provides MainActivityScope]
    fun provideHabitListViewModel(
        activity: MainActivity,
        factory: ViewModelProvider.Factory
    ): HabitListViewModel {
        return ViewModelProvider(activity, factory)[HabitListViewModel::class.java]
    }

    @[Provides MainActivityScope]
    fun provideHabitListViewModelFactory(
        getHabitListUseCase: GetHabitListUseCase,
        addHabitItemUseCase: AddHabitItemUseCase,
        deleteHabitItemUseCase: DeleteHabitItemUseCase,
        addHabitDoneUseCase: AddHabitDoneUseCase,
        deleteHabitDoneUseCase: DeleteHabitDoneUseCase
    ): ViewModelProvider.Factory = HabitListViewModel.Factory(
        getHabitListUseCase = getHabitListUseCase,
        addHabitItemUseCase = addHabitItemUseCase,
        deleteHabitItemUseCase = deleteHabitItemUseCase,
        addHabitDoneUseCase = addHabitDoneUseCase,
        deleteHabitDoneUseCase = deleteHabitDoneUseCase
    )
}

