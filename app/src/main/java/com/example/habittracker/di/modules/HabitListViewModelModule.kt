package com.example.habittracker.di.modules

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.presentation.ui.HabitListFragment
import com.example.habittracker.presentation.ui.MainActivity
import com.example.habittracker.presentation.view_models.HabitItemViewModel
import com.example.habittracker.presentation.view_models.HabitListViewModel
import dagger.Module
import dagger.Provides

@Module
class HabitListViewModelModule {
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

    @Provides
    fun provideHabitListViewModel(
//        application: Application,
        activity: MainActivity,
        factory: ViewModelProvider.Factory
    ): HabitListViewModel {
        return ViewModelProvider(activity, factory)[HabitListViewModel::class.java]
    }

//    @Provides
//    fun provideMainActivity(): MainActivity = MainActivity()

    @Provides
    fun provideHabitListViewModelFactory(
        application: Application,
    ): ViewModelProvider.Factory {
        return HabitListViewModel.Factory(application)
    }

//        interface Factory {
//
//            fun create(application: Application): ViewModelProvider.Factory
//        }
//            (
//            private val application: Application
////        private val addHabitItemUseCase: AddHabitItemUseCase,
////        private val editHabitItemUseCase: EditHabitItemUseCase,
////        private val getHabitItemUseCase: GetHabitItemUseCase,
////        private val mapper: HabitItemMapper,
////        private val habitTime: HabitTime
//    ) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            require(modelClass == HabitItemViewModel::class)
//            return HabitItemViewModel(application = application) as T
//        }
//    }

//
//        @Provides
////////    @Singleton
//    fun provideViewModelFactory(
//        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
//    ) = object : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return requireNotNull(providers[modelClass as Class<out ViewModel>]).get() as T
//        }
//    }

    //        @Provides
//        fun provideHabitListViewModel(
//            factory: ViewModelProvider.Factory,
//            target: HabitListFragment
//        ) = ViewModelProvider(target, factory)[HabitListViewModel::class.java]
//
}