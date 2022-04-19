package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.repositories.DbHabitRepository
import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.models.HabitListFilter
import com.example.habittracker.domain.models.HabitType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHabitListFromDbUseCase @Inject constructor(private val dbHabitRepository: DbHabitRepository) {

    operator fun invoke(
        habitTypeFilter: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<HabitItem>> {
        return dbHabitRepository.getHabitList(habitTypeFilter, habitListFilter)
    }
}