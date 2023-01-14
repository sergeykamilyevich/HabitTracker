package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitListFilter
import com.example.habittracker.core_api.domain.models.HabitType
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.domain.usecases.GetHabitListUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitListUseCaseImpl @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) : GetHabitListUseCase {

    override operator fun invoke(
        habitTypeFilter: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>> = dbHabitRepository.getHabitList(habitTypeFilter, habitListFilter)
}