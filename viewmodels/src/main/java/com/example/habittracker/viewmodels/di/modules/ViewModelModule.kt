package com.example.habittracker.viewmodels.di.modules

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
    fun bindFilterViewModelImpl(impl: FilterViewModel): Any
}