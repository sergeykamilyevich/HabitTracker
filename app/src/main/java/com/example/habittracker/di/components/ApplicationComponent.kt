package com.example.habittracker.di.components

import android.app.Application
import com.example.habittracker.di.modules.DataModule
import com.example.habittracker.di.modules.SubcomponentsModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
//        ApplicationModule::class,
//        PresentationModule::class,
        DataModule::class,
//        DomainModule::class,
//        HabitListFragmentModule::class,
//        SubcomponentsModule::class

    ]
)
interface ApplicationComponent
//    : AndroidInjector<App>
{

    @Component.Factory
    interface Factory
//        : AndroidInjector.Factory<App>
    {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }

    fun habitItemViewModelComponentFactory(): HabitItemViewModelComponent.Factory

    fun mainActivityComponentFactory(): MainActivityComponent.Factory

//    fun mainActivityComponent(): MainActivityComponent.Factory

//    @HabitListViewModelScope
//    fun habitListViewModelComponent(): HabitListViewModelComponent.Factory


//    fun mainActivityComponent(): MainActivityComponent


}

