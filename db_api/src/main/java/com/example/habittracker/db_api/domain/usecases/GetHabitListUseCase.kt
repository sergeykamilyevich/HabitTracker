package com.example.habittracker.db_api.domain.usecases

import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitListFilter
import com.example.habittracker.core_api.domain.models.HabitType
import kotlinx.coroutines.flow.Flow

interface GetHabitListUseCase {

    operator fun invoke(
        habitTypeFilter: HabitType?,
        habitListFilter: HabitListFilter,
    ): Flow<List<Habit>>
}