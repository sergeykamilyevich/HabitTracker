package com.example.habittracker.di.components

import android.app.Application
import com.example.habittracker.data.di.modules.CloudModule
import com.example.habittracker.data.di.modules.DataAbstractModule
import com.example.habittracker.data.di.modules.DataModule
import com.example.habittracker.di.modules.PresentationAbstractModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        CloudModule::class,
        DataModule::class,
        DataAbstractModule::class,
        PresentationAbstractModule::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }

    fun mainActivityComponentFactory(): MainActivityComponent.Factory

}

