package com.example.habittracker.viewmodels.di.modules

import com.example.habittracker.viewmodels.presentation.AuthorizationViewModel
import com.example.habittracker.viewmodels.presentation.FilterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ClassKey(FilterViewModel::class)
    @Binds
    fun bindFilterViewModel(impl: FilterViewModel): Any

    @IntoMap
    @ClassKey(AuthorizationViewModel::class)
    @Binds
    fun bindAuthorizationViewModel(impl: AuthorizationViewModel): Any
}