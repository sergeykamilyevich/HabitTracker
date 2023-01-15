package com.example.habittracker.app

import android.app.Application
import com.example.habittracker.di.components.CoreFacadeComponent
import com.example.habittracker.di.components.DbFacadeComponent
import com.example.habittracker.di.components.NetworkFacadeComponent
import com.example.habittracker.feature_habit_filter.di.components.DaggerFeatureHabitFilterComponent
import com.example.habittracker.feature_habit_filter.di.components.FeatureHabitFilterComponent
import com.example.habittracker.feature_habits.di.components.DaggerFeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponent

class App : Application(), FeatureComponentsProvider, AppWithFacade {

    override val coreFacadeComponent: CoreFacadeComponent by lazy {
        CoreFacadeComponent.init()
    }

    override val networkFacadeComponent: NetworkFacadeComponent by lazy {
        NetworkFacadeComponent.init()
    }

    override val dbFacadeComponent: DbFacadeComponent by lazy {
        DbFacadeComponent.init(context = this)
    }

    override val featureHabitsComponent: FeatureHabitsComponent by lazy {
        DaggerFeatureHabitsComponent
            .factory()
            .create(
                application = this,
                coreFacadeComponentProviders = coreFacadeComponent,
                networkFacadeComponentProviders = networkFacadeComponent,
                dbFacadeComponentProviders = dbFacadeComponent
            )
    }

    override val featureHabitFilterComponent: FeatureHabitFilterComponent by lazy {
        DaggerFeatureHabitFilterComponent
            .factory()
            .create(
                application = this,
                coreFacadeComponentProviders = coreFacadeComponent
            )
    }
}