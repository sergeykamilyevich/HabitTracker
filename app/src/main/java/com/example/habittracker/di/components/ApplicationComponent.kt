package com.example.habittracker.di.components

import android.app.Application
import com.example.habittracker.data.di.modules.DataModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }

    fun mainActivityComponentFactory(): MainActivityComponent.Factory

}

