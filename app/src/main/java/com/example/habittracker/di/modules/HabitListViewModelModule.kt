package com.example.habittracker.di.modules

import android.app.Application
import com.example.habittracker.domain.usecases.*
import com.example.habittracker.presentation.view_models.HabitListViewModel
import dagger.Module
import dagger.Provides

@Module
object HabitListViewModelModule {
//
//    @Provides
//    fun habitListViewModel(application: Application,
//                           getHabitListUseCase: GetHabitListUseCase,
//                           addHabitItemUseCase: AddHabitItemUseCase,
//                           deleteHabitItemUseCase: DeleteHabitItemUseCase,
//                           addHabitDoneUseCase: AddHabitDoneUseCase,
//                           deleteHabitDoneUseCase: DeleteHabitDoneUseCase
//    ): HabitListViewModel = HabitListViewModel(
//        application,
//        getHabitListUseCase,
//        addHabitItemUseCase,
//        deleteHabitItemUseCase,
//        addHabitDoneUseCase,
//        deleteHabitDoneUseCase
//    )
}