package com.example.habittracker.feature_habit_filter_api.di.components

import android.app.Application
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.mediators.CoreFacadeComponentProviders
import com.example.habittracker.db_api.di.mediators.DbFacadeComponentProviders
import com.example.habittracker.feature_habit_filter_api.di.modules.FeatureHabitFilterModule
import com.example.habittracker.feature_habit_filter_api.presentation.ui.BottomSheetFragment
import com.example.habittracker.network_api.di.mediators.NetworkFacadeComponentProviders
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [
        CoreFacadeComponentProviders::class,
        NetworkFacadeComponentProviders::class,
        DbFacadeComponentProviders::class,
    ],
    modules = [
        FeatureHabitFilterModule::class
    ]
)
interface FeatureHabitFilterComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application,
            coreFacadeComponentProviders: CoreFacadeComponentProviders,
            networkFacadeComponentProviders: NetworkFacadeComponentProviders,
            dbFacadeComponentProviders: DbFacadeComponentProviders,
        ): FeatureHabitFilterComponent
    }

    fun inject(bottomSheetFragment: BottomSheetFragment)

}