package com.example.habittracker.feature_habits.domain.usecases.db

import com.example.habittracker.core.domain.models.Habit
import com.example.habittracker.core.domain.models.HabitListFilter
import com.example.habittracker.core.domain.models.HabitType
import com.example.habittracker.core.domain.repositories.DbHabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//@Singleton
class GetHabitListUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    operator fun invoke(
        habitTypeFilter: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>> = dbHabitRepository.getHabitList(habitTypeFilter, habitListFilter)
}