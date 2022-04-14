package com.example.habittracker.di.modules

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.domain.usecases.*
import com.example.habittracker.presentation.ui.MainActivity
import com.example.habittracker.presentation.view_models.HabitListViewModel
import dagger.Module
import dagger.Provides

@Module
class HabitListViewModelModule {

    @Provides
    fun provideHabitListViewModel(
        activity: MainActivity,
        factory: ViewModelProvider.Factory
    ): HabitListViewModel {
        return ViewModelProvider(activity, factory)[HabitListViewModel::class.java]
    }

    @Provides
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

