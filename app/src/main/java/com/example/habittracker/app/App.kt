package com.example.habittracker.app

import android.app.Application
import android.content.Context
import com.example.habittracker.di.components.ApplicationComponent
import com.example.habittracker.di.components.DaggerApplicationComponent
import javax.inject.Singleton

@Singleton
class App : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        applicationComponent = DaggerApplicationComponent
            .factory()
            .create(application = this)
        super.onCreate()

    }
}

val Context.applicationComponent: ApplicationComponent
    get() = if (this is App) applicationComponent else this.applicationContext.applicationComponent