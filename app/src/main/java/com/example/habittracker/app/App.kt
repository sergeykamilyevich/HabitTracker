package com.example.habittracker.app

import android.app.Application
import com.example.habittracker.di.components.ApplicationComponent
import com.example.habittracker.di.components.DaggerApplicationComponent
import com.example.habittracker.di.components.NetworkFacadeComponent
import com.example.habittracker.feature_habits.di.components.DaggerFeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponentProvider
import com.example.habittracker.network_api.di.mediators.AppWithNetworkFacade
import com.example.habittracker.network_api.di.mediators.NetworkFacadeProviders
import javax.inject.Singleton

@Singleton
class App : Application(), FeatureHabitsComponentProvider, AppWithNetworkFacade {
    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        applicationComponent = DaggerApplicationComponent
            .factory()
            .create(application = this)
        getNetworkFacade()
        super.onCreate()

    }

    override fun provideFeatureHabitsComponent(): FeatureHabitsComponent {
        return DaggerFeatureHabitsComponent
            .builder()
            .application(application = this)
            .networkFacadeProviders(networkFacadeProviders = getNetworkFacade())
            .build()
    }

    override fun getNetworkFacade(): NetworkFacadeProviders {
        return networkFacadeComponent ?: NetworkFacadeComponent.init().also {
            networkFacadeComponent = it
        }
    }

    companion object {

        private var networkFacadeComponent: NetworkFacadeComponent? = null
    }
}

//val Context.applicationComponent: ApplicationComponent
//    get() = if (this is App) applicationComponent else this.applicationContext.applicationComponent