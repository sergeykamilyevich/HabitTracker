package com.example.habittracker.app

import android.app.Application
import android.content.Context
import com.example.habittracker.di.components.ApplicationComponent
import com.example.habittracker.di.components.DaggerApplicationComponent

//@Singleton
class App : Application()
{
    val applicationComponent: ApplicationComponent by lazy {
                    DaggerApplicationComponent
                .factory()
                .create(application = this)
    }
}

val Context.applicationComponent: ApplicationComponent
    get() = if (this is App) applicationComponent else this.applicationContext.applicationComponent