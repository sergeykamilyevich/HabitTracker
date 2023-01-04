package com.example.habittracker.app

import android.app.Application
import android.content.Context
import com.example.habittracker.di.components.ApplicationComponent
import com.example.habittracker.di.components.DaggerApplicationComponent
import com.example.habittracker.feature_habits.di.components.DaggerFeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponentProvider
import javax.inject.Singleton

@Singleton
class App : Application(), FeatureHabitsComponentProvider {
    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        applicationComponent = DaggerApplicationComponent
            .factory()
            .create(application = this)
        super.onCreate()

    }

    override fun provideFeatureHabitsComponent(): FeatureHabitsComponent {
        return DaggerFeatureHabitsComponent.factory().create(application = this)
    }
}

val Context.applicationComponent: ApplicationComponent
    get() = if (this is App) applicationComponent else this.applicationContext.applicationComponent