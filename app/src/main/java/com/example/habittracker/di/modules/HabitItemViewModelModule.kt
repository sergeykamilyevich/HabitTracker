package com.example.habittracker.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.di.HabitItemFragmentScope
import com.example.habittracker.domain.models.HabitTime
import com.example.habittracker.domain.usecases.*
import com.example.habittracker.presentation.mappers.HabitItemMapper
import com.example.habittracker.presentation.ui.HabitItemFragment
import com.example.habittracker.presentation.view_models.HabitItemViewModel
import dagger.Module
import dagger.Provides

@Module
object HabitItemViewModelModule {

    @[Provides HabitItemFragmentScope]
    fun provideHabitItemViewModel(
        fragment: HabitItemFragment,
        factory: ViewModelProvider.Factory
    ): HabitItemViewModel {
        return ViewModelProvider(fragment, factory)[HabitItemViewModel::class.java]
    }

    @[Provides HabitItemFragmentScope]
    fun provideHabitItemViewModelFactory(
        addHabitItemUseCase: AddHabitItemUseCase,
        editHabitItemUseCase: EditHabitItemUseCase,
        getHabitItemUseCase: GetHabitItemUseCase,
        mapper: HabitItemMapper,
        habitTime: HabitTime
    ): ViewModelProvider.Factory = HabitItemViewModel.Factory(
        addHabitItemUseCase = addHabitItemUseCase,
        editHabitItemUseCase = editHabitItemUseCase,
        getHabitItemUseCase = getHabitItemUseCase,
        mapper = mapper,
        habitTime = habitTime
    )
}