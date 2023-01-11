package com.example.habittracker.app

import android.app.Application
import com.example.habittracker.db_api.di.mediators.AppWithDbFacade
import com.example.habittracker.db_api.di.mediators.DbFacadeComponentProviders
import com.example.habittracker.di.components.DbFacadeComponent
import com.example.habittracker.di.components.NetworkFacadeComponent
import com.example.habittracker.feature_habits.di.components.DaggerFeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponentProvider
import com.example.habittracker.network_api.di.mediators.AppWithNetworkFacade
import com.example.habittracker.network_api.di.mediators.NetworkFacadeComponentProviders

class App : Application(), FeatureHabitsComponentProvider, AppWithNetworkFacade, AppWithDbFacade {

    private val networkFacadeComponent: NetworkFacadeComponent by lazy {
        NetworkFacadeComponent.init()
    }

    private val dbFacadeComponent: DbFacadeComponent by lazy {
        DbFacadeComponent.init(this)
    }

    private val featureHabitsComponent: FeatureHabitsComponent by lazy {
        DaggerFeatureHabitsComponent
            .builder()
            .application(application = this)
            .networkFacadeComponentProviders(networkFacadeComponentProviders = networkFacadeComponent())
            .dbFacadeComponentProviders(dbFacadeComponentProviders = dbFacadeComponent())
            .build()
    }

    override fun featureHabitsComponent(): FeatureHabitsComponent = featureHabitsComponent

    override fun dbFacadeComponent(): DbFacadeComponentProviders = dbFacadeComponent

    override fun networkFacadeComponent(): NetworkFacadeComponentProviders = networkFacadeComponent

}