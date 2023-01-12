package com.example.habittracker.core_api.domain.usecases.db

import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitListFilter
import com.example.habittracker.core_api.domain.models.HabitType
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitListUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    operator fun invoke(
        habitTypeFilter: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>> = dbHabitRepository.getHabitList(habitTypeFilter, habitListFilter)
}