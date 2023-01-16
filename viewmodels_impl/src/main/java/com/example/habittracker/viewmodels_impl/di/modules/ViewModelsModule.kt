package com.example.habittracker.viewmodels_impl.di.modules

import com.example.habittracker.viewmodels_api.presentation.tools.Resources
import com.example.habittracker.viewmodels_api.presentation.view_models.*
import com.example.habittracker.viewmodels_impl.presentation.view_models.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
interface ViewModelsModule {

    @IntoMap
    @ClassKey(FilterViewModel::class)
    @Binds
    fun bindFilterViewModel(impl: FilterViewModelImpl): Any

    @IntoMap
    @ClassKey(AuthorizationViewModel::class)
    @Binds
    fun bindAuthorizationViewModel(impl: AuthorizationViewModelImpl): Any

    @IntoMap
    @ClassKey(HabitItemViewModel::class)
    @Binds
    fun bindHabitItemViewModel(impl: HabitItemViewModelImpl): Any

    @IntoMap
    @ClassKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(impl: MainViewModelImpl): Any

    @[Binds]
    fun bindResources(impl: Resources.Base): Resources

    @[Binds]
    fun bindViewModelFactory(impl: ViewModelFactoryImpl): ViewModelFactory
}