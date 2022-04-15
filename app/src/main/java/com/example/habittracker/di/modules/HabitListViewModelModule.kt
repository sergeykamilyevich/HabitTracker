package com.example.habittracker.di.modules

import dagger.Module

@Module
object HabitListViewModelModule {

//    @[Provides MainActivityScope HabitListViewModelQualifier]
//    fun provideHabitListViewModel(
//        activity: MainActivity,
//        factory: ViewModelProvider.Factory
//    ): HabitListViewModel {
//        Log.d("99999", "provideHabitListViewModel")
//        return ViewModelProvider(activity, factory)[HabitListViewModel::class.java]
//    }
//
//    @[Provides MainActivityScope HabitListViewModelQualifier]
//    fun provideHabitListViewModelFactory(
//        getHabitListUseCase: GetHabitListUseCase,
//        addHabitItemUseCase: AddHabitItemUseCase,
//        deleteHabitItemUseCase: DeleteHabitItemUseCase,
//        addHabitDoneUseCase: AddHabitDoneUseCase,
//        deleteHabitDoneUseCase: DeleteHabitDoneUseCase
//    ): ViewModelProvider.Factory {
//        val factory = HabitListViewModel.Factory(
//        getHabitListUseCase = getHabitListUseCase,
//        addHabitItemUseCase = addHabitItemUseCase,
//        deleteHabitItemUseCase = deleteHabitItemUseCase,
//        addHabitDoneUseCase = addHabitDoneUseCase,
//        deleteHabitDoneUseCase = deleteHabitDoneUseCase
//        )
//        Log.d("99999", "$factory provideHabitListViewModelFactory")
//        return factory
//    }
//
//    @[Provides MainActivityScope HabitItemViewModelQualifier]
//    fun provideHabitItemViewModel(
//        activity: MainActivity,
//        factory: ViewModelProvider.Factory
//    ): HabitItemViewModel {
//        return ViewModelProvider(activity, factory)[HabitItemViewModel::class.java]
//    }
//
//    @[Provides HabitItemFragmentScope HabitItemViewModelQualifier]
//    fun provideHabitItemViewModelFactory(
//        addHabitItemUseCase: AddHabitItemUseCase,
//        editHabitItemUseCase: EditHabitItemUseCase,
//        getHabitItemUseCase: GetHabitItemUseCase,
//        mapper: HabitItemMapper,
//        habitTime: HabitTime
//    ): ViewModelProvider.Factory = HabitItemViewModel.Factory(
//        addHabitItemUseCase = addHabitItemUseCase,
//        editHabitItemUseCase = editHabitItemUseCase,
//        getHabitItemUseCase = getHabitItemUseCase,
//        mapper = mapper,
//        habitTime = habitTime
//    )
}

