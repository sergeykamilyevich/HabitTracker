package com.example.habittracker.di.modules

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.di.annotations.ViewModelKey
import com.example.habittracker.domain.usecases.*
import com.example.habittracker.presentation.ui.HabitListFragment
import com.example.habittracker.presentation.view_models.HabitListViewModel
import dagger.Module
import dagger.Provides
//import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

//@Module(includes = [HabitListFragmentModule.ProvideViewModel::class])
//abstract class HabitListFragmentModule {
//
//    @ContributesAndroidInjector(
//        modules = [
//            InjectViewModel::class
//        ]
//    )
//    abstract fun bind(): HabitListFragment
//
//    @Module
//    class InjectViewModel {
//
//        @Provides
//        fun provideHabitListViewModel(
//            factory: ViewModelProvider.Factory,
//            target: HabitListFragment
//        ) = ViewModelProvider(target, factory)[HabitListViewModel::class.java]
//
//    }
//
//    @Module
//    class ProvideViewModel {
//
//        @Provides
//        @IntoMap
//        @ViewModelKey(HabitListViewModel::class)
//        fun provideFeatureViewModel(
//            application: Application,
//            getHabitListUseCase: GetHabitListUseCase,
//            addHabitItemUseCase: AddHabitItemUseCase,
//            deleteHabitItemUseCase: DeleteHabitItemUseCase,
//            addHabitDoneUseCase: AddHabitDoneUseCase,
//            deleteHabitDoneUseCase: DeleteHabitDoneUseCase
//        ): ViewModel =
//            HabitListViewModel(
//                application,
//                getHabitListUseCase,
//                addHabitItemUseCase,
//                deleteHabitItemUseCase,
//                addHabitDoneUseCase,
//                deleteHabitDoneUseCase
//            )
//
//    }
//}