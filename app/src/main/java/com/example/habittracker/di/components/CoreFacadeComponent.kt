package com.example.habittracker.di.components

import com.example.habittracker.core.di.CoreProvidersFactory
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.mediators.CoreFacadeComponentProviders
import com.example.habittracker.viewmodels.di.modules.ViewModelModule
import com.example.habittracker.core_api.di.providers.CoreComponentProvider
import com.example.habittracker.feature_habit_filter.di.modules.HabitFilterExternalModule
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [CoreComponentProvider::class],
    modules = [HabitFilterExternalModule::class, ViewModelModule::class]
)
interface CoreFacadeComponent : CoreFacadeComponentProviders {

    companion object {

        fun init(): CoreFacadeComponent =
            DaggerCoreFacadeComponent
                .builder()
                .coreComponentProvider(CoreProvidersFactory.getCoreComponent())
                .build()
    }
}