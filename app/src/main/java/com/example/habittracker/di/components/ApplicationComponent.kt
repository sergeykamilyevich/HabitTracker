package com.example.habittracker.di.components

import android.app.Application
import com.example.habittracker.data.di.modules.CloudModule
import com.example.habittracker.data.di.modules.DataModule
import com.example.habittracker.di.modules.PresentationAbstractModule
import com.example.habittracker.feature_habits.di.modules.FeatureHabitsModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        CloudModule::class,
        DataModule::class,
        PresentationAbstractModule::class,
        FeatureHabitsModule::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }

}

