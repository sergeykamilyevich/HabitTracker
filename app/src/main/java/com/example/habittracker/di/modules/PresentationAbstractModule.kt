package com.example.habittracker.di.modules

import com.example.habittracker.feature_habits.presentation.view_models.Resources
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface PresentationAbstractModule { //TODO delete?

    @[Binds Singleton]
    fun bindResources(impl: Resources.Base): Resources
}