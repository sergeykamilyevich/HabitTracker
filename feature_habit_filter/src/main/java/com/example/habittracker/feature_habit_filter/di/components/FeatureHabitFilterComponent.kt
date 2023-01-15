package com.example.habittracker.feature_habit_filter.di.components

import android.app.Application
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.mediators.CoreFacadeComponentProviders
import com.example.habittracker.feature_habit_filter.di.modules.FeatureHabitFilterModule
import com.example.habittracker.feature_habit_filter.presentation.ui.BottomSheetFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [
        CoreFacadeComponentProviders::class
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
            coreFacadeComponentProviders: CoreFacadeComponentProviders
        ): FeatureHabitFilterComponent
    }

    fun inject(bottomSheetFragment: BottomSheetFragment)

}