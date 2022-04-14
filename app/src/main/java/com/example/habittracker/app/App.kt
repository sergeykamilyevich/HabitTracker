package com.example.habittracker.app

import android.app.Application
import android.content.Context
import com.example.habittracker.di.components.ApplicationComponent
import com.example.habittracker.di.components.DaggerApplicationComponent

//@Singleton
class App : Application()
//    , HasAndroidInjector
{
//    lateinit var application: Application
    //    @Inject
//    lateinit var FragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
//    @Inject
//    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    val applicationComponent: ApplicationComponent by lazy {
                    DaggerApplicationComponent
                .factory()
                .create(application = this)
    }

    override fun onCreate() {
        super.onCreate()
//        applicationComponent =

//                .inject(application = this)
//                .inject(this)
    }

//    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
//        TODO("Not yet implemented")
//    }

//    override fun androidInjector(): AndroidInjector<Any> {
//        return androidInjector
//    }

//    override fun androidInjector(): AndroidInjector<Activity> = androidInjector
}

val Context.applicationComponent: ApplicationComponent
    get() = if (this is App) applicationComponent else this.applicationContext.applicationComponent