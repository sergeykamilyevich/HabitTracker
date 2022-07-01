package com.example.habittracker.di.modules

import com.example.habittracker.presentation.view_models.Resources
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface PresentationAbstractModule {

    @[Binds Singleton]
    fun bindResources(impl: Resources.Base): Resources
}