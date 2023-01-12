package com.example.habittracker.app

import android.app.Application
import com.example.habittracker.core_api.di.mediators.CoreFacadeComponentProviders
import com.example.habittracker.db_api.di.mediators.DbFacadeComponentProviders
import com.example.habittracker.di.components.AppWithFacade
import com.example.habittracker.di.components.CoreFacadeComponent
import com.example.habittracker.di.components.DbFacadeComponent
import com.example.habittracker.di.components.NetworkFacadeComponent
import com.example.habittracker.feature_habits.di.components.DaggerFeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponentProvider
import com.example.habittracker.network_api.di.mediators.NetworkFacadeComponentProviders

class App : Application(), FeatureHabitsComponentProvider, AppWithFacade {

    private val coreFacadeComponent: CoreFacadeComponent by lazy {
        CoreFacadeComponent.init()
    }

    private val networkFacadeComponent: NetworkFacadeComponent by lazy {
        NetworkFacadeComponent.init()
    }

    private val dbFacadeComponent: DbFacadeComponent by lazy {
        DbFacadeComponent.init(context = this)
    }

    private val featureHabitsComponent: FeatureHabitsComponent by lazy {
        DaggerFeatureHabitsComponent
            .factory()
            .create(
                application = this,
                coreFacadeComponentProviders = coreFacadeComponent(),
                networkFacadeComponentProviders = networkFacadeComponent(),
                dbFacadeComponentProviders = dbFacadeComponent()
            )
    }

    override fun featureHabitsComponent(): FeatureHabitsComponent = featureHabitsComponent

    override fun dbFacadeComponent(): DbFacadeComponentProviders = dbFacadeComponent

    override fun coreFacadeComponent(): CoreFacadeComponentProviders = coreFacadeComponent

    override fun networkFacadeComponent(): NetworkFacadeComponentProviders = networkFacadeComponent

}