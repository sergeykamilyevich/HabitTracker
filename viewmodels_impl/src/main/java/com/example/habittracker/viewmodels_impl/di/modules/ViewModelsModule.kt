package com.example.habittracker.viewmodels_impl.di.modules

import com.example.habittracker.viewmodels_api.presentation.tools.Resources
import com.example.habittracker.viewmodels_impl.presentation.view_models.AuthorizationViewModel
import com.example.habittracker.viewmodels_impl.presentation.view_models.FilterViewModel
import com.example.habittracker.viewmodels_impl.presentation.view_models.HabitItemViewModel
import com.example.habittracker.viewmodels_impl.presentation.view_models.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
interface ViewModelsModule {

    @IntoMap
    @ClassKey(FilterViewModel::class)
    @Binds
    fun bindFilterViewModel(impl: FilterViewModel): Any

    @IntoMap
    @ClassKey(AuthorizationViewModel::class)
    @Binds
    fun bindAuthorizationViewModel(impl: AuthorizationViewModel): Any

    @IntoMap
    @ClassKey(HabitItemViewModel::class)
    @Binds
    fun bindHabitItemViewModel(impl: HabitItemViewModel): Any

    @IntoMap
    @ClassKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(impl: MainViewModel): Any

    @[Binds]
    fun bindResources(impl: Resources.Base): Resources
}