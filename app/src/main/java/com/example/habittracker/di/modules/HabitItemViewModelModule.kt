package com.example.habittracker.di.modules

import android.app.Application
import com.example.habittracker.domain.usecases.AddHabitItemUseCase
import com.example.habittracker.domain.usecases.EditHabitItemUseCase
import com.example.habittracker.domain.usecases.GetHabitItemUseCase
import com.example.habittracker.presentation.view_models.HabitItemViewModel
import dagger.Module
import dagger.Provides

@Module
class HabitItemViewModelModule {

    @Provides
    fun provideHabitItemViewModel(application: Application): HabitItemViewModel {
        return HabitItemViewModel(
            application = application
        )
    }
}