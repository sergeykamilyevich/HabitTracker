package com.example.habittracker.feature_habit_filter.di.components

import android.app.Application
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.mediators.CoreFacadeComponentProviders
import com.example.habittracker.feature_habit_filter.presentation.ui.BottomSheetFragment
import com.example.habittracker.viewmodels_api.di.mediators.ViewModelsFacadeComponentProviders
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [
        CoreFacadeComponentProviders::class,
        ViewModelsFacadeComponentProviders::class,
    ]
)
interface FeatureHabitFilterComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application,
            coreFacadeComponentProviders: CoreFacadeComponentProviders,
            viewModelsFacadeComponentProviders: ViewModelsFacadeComponentProviders,
        ): FeatureHabitFilterComponent
    }

    fun inject(bottomSheetFragment: BottomSheetFragment)

}