package com.example.habittracker.app

import android.app.Application
import com.example.habittracker.di.components.ApplicationComponent
import com.example.habittracker.di.components.DaggerApplicationComponent
import com.example.habittracker.di.components.NetworkFacadeComponent
import com.example.habittracker.feature_habits.di.components.DaggerFeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponentProvider
import com.example.habittracker.network_api.di.mediators.AppWithNetworkFacade
import com.example.habittracker.network_api.di.mediators.NetworkFacadeComponentProviders

class App : Application(), FeatureHabitsComponentProvider, AppWithNetworkFacade {

    val applicationComponent: ApplicationComponent by lazy { //TODO delete?
        DaggerApplicationComponent
            .factory()
            .create(application = this)
    }

    private val networkFacadeComponent: NetworkFacadeComponent by lazy {
        NetworkFacadeComponent.init()
    }

//    override fun onCreate() {
//        networkFacadeComponent()
//        super.onCreate()
//    }

    override fun provideFeatureHabitsComponent(): FeatureHabitsComponent {
        return DaggerFeatureHabitsComponent
            .builder()
            .application(application = this)
            .networkComponentFacadeProviders(networkFacadeComponentProviders = networkFacadeComponent())
            .build()
    }

    override fun networkFacadeComponent(): NetworkFacadeComponentProviders {
        return networkFacadeComponent
    }

    companion object {

//        private var networkFacadeComponent: NetworkFacadeComponent? = null
    }
}

//val Context.applicationComponent: ApplicationComponent
//    get() = if (this is App) applicationComponent else this.applicationContext.applicationComponent