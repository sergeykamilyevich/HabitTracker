package com.example.habittracker.di.modules

import android.app.Application
import com.example.habittracker.domain.models.HabitTime
import com.example.habittracker.domain.usecases.AddHabitItemUseCase
import com.example.habittracker.domain.usecases.EditHabitItemUseCase
import com.example.habittracker.domain.usecases.GetHabitItemUseCase
import com.example.habittracker.presentation.mappers.HabitItemMapper
import com.example.habittracker.presentation.view_models.HabitItemViewModel
import dagger.Module
import dagger.Provides

@Module
class HabitItemViewModelModule {

    @Provides
    fun provideHabitItemViewModel(
        addHabitItemUseCase: AddHabitItemUseCase,
        editHabitItemUseCase: EditHabitItemUseCase,
        getHabitItemUseCase: GetHabitItemUseCase,
        mapper: HabitItemMapper,
        habitTime: HabitTime

    ): HabitItemViewModel {
        return HabitItemViewModel(
            addHabitItemUseCase = addHabitItemUseCase,
            editHabitItemUseCase = editHabitItemUseCase,
            getHabitItemUseCase = getHabitItemUseCase,
            mapper = mapper,
            habitTime = habitTime
        )
    }
}