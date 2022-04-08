package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.repositories.HabitRepository
import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.models.HabitListFilter
import com.example.habittracker.domain.models.HabitType
import kotlinx.coroutines.flow.Flow

class GetHabitListUseCase(private val habitRepository: HabitRepository) {

    operator fun invoke(
        habitTypeFilter: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<HabitItem>> {
        return habitRepository.getHabitList(habitTypeFilter, habitListFilter)
    }
}