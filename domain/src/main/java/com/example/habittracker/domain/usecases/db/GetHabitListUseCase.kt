package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitListFilter
import com.example.habittracker.domain.models.HabitType
import com.example.habittracker.domain.repositories.DbHabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHabitListUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    operator fun invoke(
        habitTypeFilter: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>> = dbHabitRepository.getHabitList(habitTypeFilter, habitListFilter)
}