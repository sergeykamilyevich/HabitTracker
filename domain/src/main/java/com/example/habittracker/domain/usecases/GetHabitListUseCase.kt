package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.HabitRepository
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitListFilter
import com.example.habittracker.domain.entities.HabitType
import kotlinx.coroutines.flow.Flow

class GetHabitListUseCase(private val habitRepository: HabitRepository) {

    operator fun invoke(
        habitTypeFilter: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<HabitItem>> {
        return habitRepository.getHabitList(habitTypeFilter, habitListFilter)
    }
}